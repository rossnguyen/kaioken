package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.SerializableLatLng
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.utils.Constraint
import agv.kaak.vn.kaioken.utils.application.MyApplication
import agv.kaak.vn.kaioken.utils.firebase.MyFirebaseMessagingService
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.os.PersistableBundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.maps.model.LatLng
import io.socket.client.Socket
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

abstract class BaseActivity : AppCompatActivity() {

    @field:[Inject Named("api")]
    lateinit var mRetrofitAPI: Retrofit

    @field:[Inject Named("image")]
    lateinit var mRetrofitImage: Retrofit

    @Inject
    lateinit var mSocket: Socket

    @Inject
    lateinit var errorCode: HashMap<Int, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onResume() {
        super.onResume()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
        MyFirebaseMessagingService.numberMessage = 0
    }

    fun getRetrofitAndErrorMap() {
        //  init components
        //(application as MyApplication).getRetrofitAndErrorMap().inject(this)
        Log.d("****", "co go get appcomponent")
        (application as MyApplication).getAppComponent().inject(this)
        if (Constraint.mRetrofit == null) {
            Constraint.mRetrofit = mRetrofitAPI
            Constraint.mRetrofitImage = mRetrofitImage
        }

        /*if (Constraint.BASE_SOCKET == null) {
            Constraint.BASE_SOCKET = mSocket
            Constraint.BASE_SOCKET?.once(Socket.EVENT_CONNECT) {
                runOnUiThread {
                    onConnectSocketSuccess()
                    Log.d("****Socket","Connected 1")
                }
            }
            Constraint.BASE_SOCKET?.connect()
        }*/

        Constraint.errorMap = errorCode

//        set up portrait or landscape screen
        requestedOrientation = if (applicationContext.resources.getBoolean(R.bool.landscape)) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.d("${Constraint.TAG_LOG}LifeCircle", "Co do save instance state")
        if (Constraint.uid != "-1" && Constraint.sid != "") {
            with(receiver = outState!!) {
                putInt("UID", Constraint.uid!!.toInt())
                putString("SID", Constraint.sid!!)
                putInt("ID_STORE_ORDERING", Constraint.ID_STORE_ORDERING)
                putInt("ID_TABLE_ORDERING", Constraint.ID_TABLE_ORDERING)
                putBoolean("IS_ADD_FOOD", Constraint.IS_ADD_FOOD)
                putInt("ID_ORDERING", Constraint.ID_ORDERING)
                putString("NAME_STORE_ORDERING", Constraint.NAME_STORE_ORDERING)
                putString("ADDRESS_STORE_ORDERING", Constraint.ADDRESS_STORE_ORDERING)
                putString("NAME_CUSTOMER", Constraint.NAME_CUSTOMER)
                if (Constraint.myLocation != null) {
                    val location = SerializableLatLng(Constraint.myLocation!!.latitude, Constraint.myLocation!!.longitude)
                    putSerializable("LOCATION", location)
                }
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        try {
            Log.d("${Constraint.TAG_LOG}LifeCircle", "Co do restore instance state")
            with(savedInstanceState!!) {
                Constraint.ID_STORE_ORDERING = getInt("ID_STORE_ORDERING")
                Constraint.ID_TABLE_ORDERING = getInt("ID_TABLE_ORDERING")
                Constraint.IS_ADD_FOOD = getBoolean("IS_ADD_FOOD")
                Constraint.ID_ORDERING = getInt("ID_ORDERING")
                Constraint.NAME_STORE_ORDERING = getString("NAME_STORE_ORDERING")
                Constraint.ADDRESS_STORE_ORDERING = getString("ADDRESS_STORE_ORDERING")
                Constraint.NAME_CUSTOMER = getString("NAME_CUSTOMER")

                if (getSerializable("LOCATION") != null) {
                    val myLocation = getSerializable("LOCATION") as SerializableLatLng
                    Constraint.myLocation = LatLng(myLocation.latitude, myLocation.longitude)
                }

                /*if (Constraint.BASE_SOCKET == null) {
                    Constraint.uid = getInt("UID", -1).toString()
                    Constraint.sid = getString("SID", "")
                    getRetrofitAndSocket()
                }*/
            }
        } catch (ex: Exception) { }
    }

    fun getRetrofitAndSocket() {

        if (Constraint.BASE_SOCKET != null && Constraint.BASE_SOCKET!!.connected())
            Constraint.BASE_SOCKET?.disconnect()

        (application as MyApplication).restartAppComponent().inject(this)
        Constraint.mRetrofit = mRetrofitAPI
        Constraint.mRetrofitImage = mRetrofitImage
        Constraint.BASE_SOCKET = mSocket
        //connect socket success
        Constraint.BASE_SOCKET?.once(Socket.EVENT_CONNECT) {
            runOnUiThread {
                onConnectSocketSuccess()
                Log.d("****Socket", "Connected 2")
            }
        }

        Constraint.BASE_SOCKET?.once(Socket.EVENT_CONNECT_TIMEOUT) {
            runOnUiThread {
                onConnectSocketFail()
                Log.d("****Socket", "Connected 2")
            }
        }
        Constraint.BASE_SOCKET?.connect()

    }

    fun showMessage(msg: String) {
        GlobalHelper.showMessage(applicationContext, msg, true)
    }

    fun forceCloseApp() {
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(homeIntent)
    }

    fun showKeyboard(view: View) {
        view.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getWidthScreen(): Int {
        val displayMetric = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(displayMetric)
        return displayMetric.widthPixels
    }

    fun getHeightScreen(): Int {
        val displayMetric = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(displayMetric)
        return displayMetric.heightPixels
    }

    fun getDimenPixel(id: Int): Int {
        return this.resources.getDimensionPixelOffset(id)
    }

    fun showDialogMessage(title: String?, message: String?) {
        AlertDialog.Builder(this)
                .setMessage(message)
                .setNegativeButton(resources.getString(R.string.all_close)) { dialog, which ->
                    dialog.dismiss()
                }
                .setTitle(title)
                .setCancelable(true)
                .show()
    }

    abstract fun onConnectSocketSuccess()
    abstract fun onConnectSocketFail()
}