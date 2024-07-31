package agv.kaak.vn.kaioken.fragment.home


import android.os.Bundle
import android.support.v4.app.Fragment

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.activity.HomeActivity
import agv.kaak.vn.kaioken.activity.OrderActivity
import agv.kaak.vn.kaioken.activity.ProcessActivity
import agv.kaak.vn.kaioken.entity.define.UseType
import agv.kaak.vn.kaioken.entity.result.DetailStoreResult
import agv.kaak.vn.kaioken.entity.result.InfoScanToGoStoreResult
import agv.kaak.vn.kaioken.fragment.base.BaseSocketFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.ScanQRToGoStoreContract
import agv.kaak.vn.kaioken.mvp.presenter.ScanQRToGoStorePresenter
import agv.kaak.vn.kaioken.utils.BaseSocketEmit
import agv.kaak.vn.kaioken.utils.Constraint
import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.location.Location
import android.os.CountDownTimer
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.fragment_scan_qr.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.FileNotFoundException
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 *
 */
class ScanQRFragment : BaseSocketFragment(), ScanQRToGoStoreContract.View {
    companion object {
        val DATA_STORE_SEND = "DATA_STORE_SEND"
        const val RequestCameraPermissionID = 1001
    }

    val PICK_IMAGE = 1


    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private lateinit var camera: Camera
    private var flashIsOn = false
    private var cameraIsOn = false

    private var listResult: ArrayList<String> = ArrayList()
    private lateinit var dataRestaurant: DetailStoreResult

    private val baseSocketEmit = BaseSocketEmit()
    private val scanQRToGoStorePresenter = ScanQRToGoStorePresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_qr, container, false)
    }

    override fun getData() {
        Constraint.TYPE_USE = UseType.LOCAL_HAVE_TABLE
    }

    override fun loadData() {

    }

    override fun onPause() {
        super.onPause()
        doStopScan()
    }

    override fun listenSocket() {
        Constraint.BASE_SOCKET?.on("getOrderByTableId", onGetOrderByTableIdListener)
    }

    override fun offSocket() {
        Constraint.BASE_SOCKET?.off("getOrderByTableId")
    }

    override fun setupComponentStatusSocket(): TextView? {
        return null
    }

    //get Camera to turn on/off flash light
    private fun getCamera(cameraSource: CameraSource): Camera? {
        val declaredFields = CameraSource::class.java.declaredFields

        for (field in declaredFields) {
            if (field.type == Camera::class.java) {
                field.isAccessible = true
                try {
                    return field.get(cameraSource) as Camera
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
                break
            }
        }
        return null
    }

    //turn on/off flash light when click button flash
    private fun switchFlash() {
        camera = getCamera(cameraSource)!!
        try {
            flashIsOn = !flashIsOn
            val param = camera.parameters
            param.flashMode = if (flashIsOn) Camera.Parameters.FLASH_MODE_TORCH else Camera.Parameters.FLASH_MODE_OFF
            camera.parameters = param

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun initCamera() {
        barcodeDetector = BarcodeDetector.Builder(activity!!.applicationContext)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build()

        cameraSource = CameraSource.Builder(activity!!.applicationContext, barcodeDetector)
                //Sets the camera to use
                .setFacing(CameraSource.CAMERA_FACING_BACK)

                //Sets the requested frame rate in frames per second.
                //.setRequestedFps(30.0f)
                //Sets the desired width and height of the camera frames in pixels.
                .setRequestedPreviewSize(640, 480)
                //Sets whether to enable camera auto focus.
                .setAutoFocusEnabled(true)
                .build()
    }

    override fun addEvent() {
        //hide buton back if on home
        if (activityParent is HomeActivity)
            ibtnBack.visibility = View.GONE

        initCamera()

        ibtnFlash.setOnClickListener(View.OnClickListener {
            if (!cameraIsOn)
                return@OnClickListener
            switchFlash()
            if (flashIsOn)
                ibtnFlash.setImageResource(R.drawable.ic_flash_off)
            else
                ibtnFlash.setImageResource(R.drawable.ic_flash_on)
        })
        btnMyQRCode.setOnClickListener { GlobalHelper.showMessage(context, activity!!.resources.getString(R.string.all_feature_is_developing), true) }
        btnScanToPay.setOnClickListener { GlobalHelper.showMessage(context, activity!!.resources.getString(R.string.all_feature_is_developing), true) }
        btnScanImage.setOnClickListener {
            //show activity to choose image want to scan
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE)
        }

        btnRescan.setOnClickListener { doStartScan() }

        //Get value scan
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {

                val barCodes = detections.detectedItems

                if (barCodes.size() != 0) {
                    activity!!.runOnUiThread { doStopScan(barCodes.valueAt(0).displayValue) }
                }
            }
        })

        ibtnBack.setOnClickListener {
            if (onBackButtonPressListener != null) {
                doStopScan()
                onBackButtonPressListener!!.onClick(ibtnBack)
            }

        }

        layoutProgress.setOnClickListener {
            //nothing to do
        }

        //Start camera when surfaceview created, stop camera when surface destroyed
        scanView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        ActivityCompat.requestPermissions(activity as HomeActivity, arrayOf(Manifest.permission.CAMERA), RequestCameraPermissionID)
                        return
                    }
                    doStartScan()
                } catch (ex: Exception) {
                    GlobalHelper.showMessage(activity!!.applicationContext, "error:" + ex.message, true)
                }

            }

            override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {

            }

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
                doStopScan()
                //layoutCenter.setVisibility(View.VISIBLE);
                //layoutBackground.setVisibility(View.VISIBLE);
            }
        })
    }


    /**
     * handle when stop scan:
     * stop camera
     * show layout scan now
     * hide button stop scan
     */
    private fun doStopScan(value: String) {
        cameraIsOn = false
        flashIsOn = false
        ibtnFlash.setImageResource(R.drawable.ic_flash_on)
        cameraSource.stop()
        layoutBorder.visibility = View.VISIBLE
        layoutBackground.visibility = View.VISIBLE
        btnRescan.visibility = View.VISIBLE
        //Do quét quá nhạy nên đôi khi camera stop không kịp thì đã quét được thêm 1 mã nữa và thành ra add order 2 lần
        //Dùng cách này thì chỉ add 1 lần
        if (listResult.size == 0) {
            listResult.add(value)
            if (GlobalHelper.networkIsConnected(activityParent)) {
                layoutProgress.visibility = View.VISIBLE

//                scanQRToGoStorePresenter.getStoreFromQR(value, 10.837295, 106.703619)
//                scanQRToGoStorePresenter.getStoreFromQR(value, 10.825538, 106.71581022)
                if (Constraint.myLocation != null) {
                    scanQRToGoStorePresenter.getStoreFromQR(value, Constraint.myLocation!!.latitude, Constraint.myLocation!!.longitude)
                } else
                    Handler().postDelayed({
                        if (Constraint.myLocation != null)
                            scanQRToGoStorePresenter.getStoreFromQR(value, Constraint.myLocation!!.latitude, Constraint.myLocation!!.longitude)
                        else {
                            layoutProgress.visibility = View.GONE
                            showDialogMessage(resources.getString(R.string.something_went_wrong), resources.getString(R.string.scan_fail_cause_location))
                        }
                    }, 5000)
            }

        }
    }

    fun doStopScan() {
        cameraIsOn = false
        if (flashIsOn)
            switchFlash()
        ibtnFlash.setImageResource(R.drawable.ic_flash_on)
        cameraSource.stop()
        layoutBorder.visibility = View.INVISIBLE
        layoutBackground.visibility = View.VISIBLE
        btnRescan.visibility = View.VISIBLE
    }

    /**
     * handle when start scan:
     * show button stop scan
     * hide layout scan now
     * start camera
     */
    fun doStartScan() {
        cameraIsOn = true
        listResult.clear()
        try {
            if (ActivityCompat.checkSelfPermission(activity!!.applicationContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity!! as HomeActivity, arrayOf(Manifest.permission.CAMERA), RequestCameraPermissionID)
                return
            }

            cameraSource.start(scanView.holder)
            flashIsOn = false
            ibtnFlash.setImageResource(R.drawable.ic_flash_on)
            layoutBorder.visibility = View.VISIBLE
            layoutBackground.visibility = View.GONE
            btnRescan.visibility = View.GONE
        } catch (ex: Exception) {
            showDialogMessage(mContext.resources.getString(R.string.something_went_wrong),mContext.resources.getString(R.string.all_please_try_again))
        }
    }

    private val onGetOrderByTableIdListener = Emitter.Listener {
        //processDialog.dismiss()
        activityParent.runOnUiThread {
            Log.e("${Constraint.TAG_LOG}GetOrderByTableId", it[0].toString())
            layoutProgress?.visibility = View.GONE
            Constraint.BASE_SOCKET?.off(Socket.EVENT_CONNECT)
            layoutProgress?.visibility = View.GONE
            val jsonObjectResult = it[0] as JSONObject
            try {
                val status = jsonObjectResult.getInt("status")
                //if not exist order: move to activity choose foodWithTopping
                if (status == 0) {
                    openActivityChooseRoom(dataRestaurant)
                }
                //if exist order:
                //          - store allow order local: go to process
                //          - store deny order local: go to choose foodWithTopping
                else {
                    if (dataRestaurant.isAllowOrderLocal()) {
                        val orderId = jsonObjectResult.getJSONObject("result")
                                .getJSONObject("order")
                                .getInt("order_id")
                        openActivityProcess(orderId)
                    } else
                        openActivityChooseRoom(dataRestaurant)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    private fun openActivityChooseRoom(dataRestaurant: DetailStoreResult) {
        val intent = Intent(activityParent, OrderActivity::class.java)
        startActivity(intent)
    }

    private fun openActivityProcess(orderId: Int) {
        val intent = Intent(activityParent, ProcessActivity::class.java)
        intent.putExtra(Constraint.ORDER_ID_SEND, orderId)
        startActivity(intent)

        //startActivity(Intent(activityParent,TestActivity::class.java))
    }

    override fun getStoreFromQRSuccess(data: InfoScanToGoStoreResult) {
        dataRestaurant = data.dataRestaurant!!
        Constraint.ID_STORE_ORDERING = data.pageId!!
        Constraint.ID_TABLE_ORDERING = data.tableId!!
        Constraint.NAME_STORE_ORDERING = data.dataRestaurant?.name!!
        Constraint.ADDRESS_STORE_ORDERING = data.dataRestaurant?.address!!

        //check status socket before emit
        if (!GlobalHelper.networkIsConnected(activityParent))
            return
        //show dialog handling

        layoutProgress?.visibility = View.VISIBLE

        var alreadyEmitSocket = false

        object : CountDownTimer(Constraint.timeOutSocket, 500L) {
            override fun onFinish() {
                if (layoutProgress?.visibility == View.VISIBLE) {
                    layoutProgress?.visibility = View.GONE
                    showDialogMessage(mContext.resources.getString(R.string.something_went_wrong),mContext.resources.getString(R.string.all_please_try_again))
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                if (Constraint.BASE_SOCKET!!.connected() && !alreadyEmitSocket) {
                    baseSocketEmit.getOrderByTableId(data.tableId)
                    Constraint.BASE_SOCKET?.on(Socket.EVENT_CONNECT, Emitter.Listener {
                        baseSocketEmit.getOrderByTableId(data.tableId)
                    })
                    alreadyEmitSocket = true
                }
            }
        }.start()

        /*if (socketReady) {
            baseSocketEmit.getOrderByTableId(data.tableId)
            Constraint.BASE_SOCKET?.on(Socket.EVENT_CONNECT, Emitter.Listener {
                baseSocketEmit.getOrderByTableId(data.tableId)
            })
        } else
            Handler().postDelayed({
                baseSocketEmit.getOrderByTableId(data.tableId)
                Constraint.BASE_SOCKET?.on(Socket.EVENT_CONNECT) {
                    baseSocketEmit.getOrderByTableId(data.tableId)
                }
            }, Constraint.delayBeforeOnSocket)*/
    }

    override fun getStoreFromQRFail(message: String?) {
        //processDialog.dismiss()
        layoutProgress?.visibility = View.GONE
        GlobalHelper.showMessage(mContext, message!!, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICK_IMAGE -> {
                if (data != null) {
                    try {
                        val inputStream = context!!.contentResolver.openInputStream(data.data!!)
                        val bufferedInputStream = BufferedInputStream(inputStream!!)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        //Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.qr_code);
                        val frame = Frame.Builder().setBitmap(bitmap).build()
                        val barcodes = barcodeDetector.detect(frame)
                        // Check if at least one barcode was detected
                        if (barcodes.size() != 0) {
                            // Display the QR code's message
                            //val myLocation = activity.getSharedPreferences(Constraint.Companion.getSHARE_PRE_INFO_USER(), Context.MODE_PRIVATE).getString(Constraint.Companion.getINFO_USER_LOCATION(), "")

                            //val lat = 10.837295
                            //val lng = 106.703619
//                        double lat = Double.parseDouble(myLocation.split(",")[0]);
//                        double lng = Double.parseDouble(myLocation.split(",")[1]);
//                        presenter.getRestaurantFromQR(barcodes.valueAt(0).displayValue, lat, lng)
                        } else {
                            GlobalHelper.showMessage(context, activity!!.resources.getString(R.string.scan_can_not_detect), true)
                            //layoutCenter.setVisibility(View.VISIBLE);
                        }
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }

                }
            }
        }
    }

    fun grantPermissionCameraSuccess() {
        doStartScan()
    }

    var onBackButtonPressListener: View.OnClickListener? = null
}
