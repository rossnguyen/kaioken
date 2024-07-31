package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.service.FloattingViewService
import agv.kaak.vn.kaioken.fragment.finance.FinanceFragment
import agv.kaak.vn.kaioken.fragment.home.*
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.CheckTokenContract
import agv.kaak.vn.kaioken.utils.Constraint
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.takusemba.spotlight.OnSpotlightStateChangedListener
import com.takusemba.spotlight.OnTargetStateChangedListener
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.shape.Circle
import com.takusemba.spotlight.target.SimpleTarget
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity(), CheckTokenContract.View,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<LocationSettingsResult> {
    companion object {
        const val LOCATION_SEND = "LOCATION_SEND"

        const val TAB_PROMOTION = 0
        const val TAB_HOME = 1
        const val TAB_SCAN = 2
        const val TAB_NOTIFY = 3
        const val TAB_KI = 4
        //const val TAB_PROFILE = 5

        const val TAB_PROMOTION_TAB = "PROMOTION"
        const val TAB_MAP_TAG = "MAP"
        const val TAB_SCAN_TAG = "SCAN"
        const val TAB_KI_TAG = "KI"
        const val TAB_NOTIFICATION_TAG = "NOTIFICATION"
    }

    private val REQUEST_CHECK_SETTINGS_GPS = 0x1
    private val REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2

    private var googleApiClient: GoogleApiClient? = null
    private var locationRequest: LocationRequest? = null

    private var promotionFragment: PromotionFragment? = null
    private var scanQRFragment: ScanQRFragment? = null
    private var mapFragment: MapFragment? = null
    private var moneyManagement: FinanceFragment? = null
    private var kiFragment: KiFragment? = null
    private var notificationFragment: NotificationFragment? = null
    private var fragmentNew: Fragment? = null
    var fragmentIsShowing = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //checkTokenPresenter.checkToken(Constraint.uid!!, Constraint.sid!!)

        initBottomNavigation()
        addEvent()
        loadData()
    }

    override fun onResume() {
        super.onResume()
        if (locationRequest == null)
            setUpLocationRequest()
        if (googleApiClient == null) {
            setUpGClient()
        } else {
            if (!googleApiClient!!.isConnected)
                googleApiClient!!.connect()
            else {
                //this case when user accept permission location
                googleApiClient!!.disconnect()
                googleApiClient!!.connect()
            }

            //only show dialog get location if on tab map and map is showing
            /*if (fragmentIsShowing == TAB_MAP_TAG && MapFragment.mapIsShowing)
                (fragmentNew as MapFragment).showDialogGettingLocation()*/
        }
    }

    override fun onConnectSocketSuccess() {

    }

    override fun onConnectSocketFail() {

    }

    private fun addEvent() {
        ibtnScan.setOnClickListener {
            layoutBottomNavigate.currentItem = 2
        }
    }

    override fun checkTokenValid() {
        if (!Constraint.SERVICE_IS_ON) {
            startService(Intent(this@HomeActivity, FloattingViewService::class.java))
            Constraint.SERVICE_IS_ON = true
        }
    }

    override fun checkTokenInValid(msg: String?) {
        val builder = AlertDialog.Builder(this@HomeActivity)
        builder.setMessage(resources.getString(R.string.alert_token_invalid_message) + " " + msg)
                .setNegativeButton(resources.getString(R.string.all_ok)) { _, _ ->
                    GlobalHelper.doLogout(this@HomeActivity)
                }
                .setCancelable(false)
                .show()
    }

    override fun checkTokenError(msg: String?) {
        Log.d("${Constraint.TAG_LOG}TokenError", msg)
        if (GlobalHelper.networkIsConnected(this))
            GlobalHelper.showMessage(applicationContext, resources.getString(R.string.all_error_global), false)
    }

    private fun initBottomNavigation() {
        /*var drawableNews = getDrawable(R.mipmap.ic_star_money)
        drawableNews = ImageHelper.scaleImage(this, drawableNews, 1.2f)*/
        val itemNews = AHBottomNavigationItem(resources.getString(R.string.all_promotion), getDrawable(R.mipmap.ic_star_money))
        layoutBottomNavigate.addItem(itemNews)

        val itemMap = AHBottomNavigationItem(resources.getString(R.string.map_item_bottom_navigate_store), getDrawable(R.drawable.ic_store))
        layoutBottomNavigate.addItem(itemMap)

        val itemScan = AHBottomNavigationItem(resources.getString(R.string.map_item_bottom_navigate_qr_scan), getDrawable(R.drawable.ic_qr_code))
        layoutBottomNavigate.addItem(itemScan)

        val itemNotification = AHBottomNavigationItem(resources.getString(R.string.map_item_bottom_navigate_order), getDrawable(R.drawable.ic_order_history))
        layoutBottomNavigate.addItem(itemNotification)

        val itemKiPay = AHBottomNavigationItem(resources.getString(R.string.map_item_bottom_navigate_ki), getDrawable(R.drawable.ic_wallet))
        layoutBottomNavigate.addItem(itemKiPay)

        /*val itemProfile = AHBottomNavigationItem(resources.getString(R.string.map_item_bottom_navigate_profile), getDrawable(R.drawable.ic_profile))
        layoutBottomNavigate.addItem(itemProfile)*/

        layoutBottomNavigate.defaultBackgroundColor = ContextCompat.getColor(applicationContext, R.color.colorMasterialGrey_4)
        layoutBottomNavigate.accentColor = ContextCompat.getColor(applicationContext, R.color.colorPrimaryButton)

        //layoutBottomNavigate.accentColor=resources.getColor(R.color.colorAccent)
        layoutBottomNavigate.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
        layoutBottomNavigate.currentItem = -1

        layoutBottomNavigate.setOnTabSelectedListener { position, _ ->
            when (position) {
                TAB_PROMOTION -> setFragmentShowing(TAB_PROMOTION_TAB)
                TAB_HOME -> {
                    Log.d("****GoMap", "from bottom navigation")
                    setFragmentShowing(TAB_MAP_TAG)
                }
                TAB_SCAN -> setFragmentShowing(TAB_SCAN_TAG)
                TAB_NOTIFY -> setFragmentShowing(TAB_NOTIFICATION_TAG)
                TAB_KI -> setFragmentShowing(TAB_KI_TAG)
            }
            if (position != TAB_SCAN) {
                ibtnScan.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.colorBlackPrimary))
                layoutScan.setBackgroundResource(R.drawable.custom_background_circle_white_border_black)
            }

            return@setOnTabSelectedListener true
        }
    }

    private fun initPromotionFragment(): Fragment? {
        promotionFragment = PromotionFragment()
        promotionFragment?.onFeaturesClickListenter = object : PromotionFragment.OnFeaturesClickListener {
            override fun onKiClick() {
                layoutBottomNavigate.currentItem = TAB_KI
            }

            override fun onNotifyClick() {
                layoutBottomNavigate.currentItem = TAB_NOTIFY
            }
        }
        return promotionFragment
    }

    fun initMapFragment(): Fragment? {
        mapFragment = MapFragment()
        return mapFragment
    }

    fun initScanFragment(): Fragment? {
        scanQRFragment = ScanQRFragment()
        val bundle = Bundle()
        bundle.putParcelable(Constraint.DATA_SEND, Constraint.myLocation)
        scanQRFragment?.arguments = bundle
        return scanQRFragment
    }

    fun initKiFragment(): Fragment? {
        kiFragment = KiFragment()
        return kiFragment
    }

    private fun initNotificationFragment(): Fragment? {
        notificationFragment = NotificationFragment()
        return notificationFragment
    }

    fun loadData() {
        //setFragmentShowing(TAB_MAP_TAG)
        //show notification if call from notification firebase
        //else show map
        if (Constraint.callFromNotification)
            layoutBottomNavigate.currentItem = TAB_NOTIFY
        else
            layoutBottomNavigate.currentItem = TAB_PROMOTION

        if (isFirstUse()) {
            Handler().postDelayed({
                showSpotLight()
            }, 1000)
        }
    }


    /**
     * load fragment when choose tab
     * if is first: add fragment
     * else hide old fragment and show this fragment
     */
    private fun setFragmentShowing(TAG: String) {
        if (fragmentIsShowing == TAG)
            return
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()

        transaction.setCustomAnimations(R.anim.fade_in_400, R.anim.fade_out_400)
        //get fragment old to hide
        val fragmentOld = manager.findFragmentByTag(fragmentIsShowing)
        if (fragmentOld != null) {
            //if fragment scan is showing: stop camera before hide
            (fragmentOld as? ScanQRFragment)?.doStopScan()

            transaction.hide(fragmentOld)

            (fragmentOld as? NotificationFragment)?.onDestroy()
        }

        if (TAG == TAB_NOTIFICATION_TAG) {
            fragmentNew = NotificationFragment()
            transaction.add(R.id.layoutContent, fragmentNew, TAG)
        } else
            fragmentNew = manager.findFragmentByTag(TAG)
        //if fragmentNew is new: add fragment
        //else show fragmentNew
        if (fragmentNew == null) {
            fragmentNew = when (TAG) {
                TAB_PROMOTION_TAB -> initPromotionFragment()
                TAB_MAP_TAG -> initMapFragment()
                TAB_SCAN_TAG -> initScanFragment()
                TAB_KI_TAG -> initKiFragment()
                TAB_NOTIFICATION_TAG -> initNotificationFragment()
                else -> null
            }
            transaction.add(R.id.layoutContent, fragmentNew, TAG)
        } else {
            transaction.show(fragmentNew)

            //start camera if fragment new is scan

            if (fragmentNew is ScanQRFragment)
                scanQRFragment?.doStartScan()

            //if tab notification: reload data
            /*if (TAG === TAB_NOTIFICATION_TAG)
                (fragmentNew as NotificationFragment).reloadData()*/
            //reset title for tab
        }
        transaction.commitAllowingStateLoss()
        //Save fragment is showing to hide if choose other item
        fragmentIsShowing = TAG

        //highlight button scan
        if (fragmentNew is ScanQRFragment) {
            ibtnScan.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.colorPrimaryButton))
            layoutScan.setBackgroundResource(R.drawable.custom_background_circle_white_border_violet)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ScanQRFragment.RequestCameraPermissionID -> {
                if (recheckPermissionCamera())
                    scanQRFragment?.grantPermissionCameraSuccess()
            }
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                val permissionLocation = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    /*Log.e("${Constraint.TAG_LOG}AcceptLocation","ok")
                    setupRealTimeLocation()*/
                    startActivity(Intent(this, SplashActivity::class.java))
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0x1 -> when (resultCode) {
                Activity.RESULT_OK -> {
                    if (googleApiClient != null)
                        if (googleApiClient!!.isConnected)
                            requestLocationUpdate()
                }
                Activity.RESULT_CANCELED -> {
                    Log.d("****PermissionLocation", "GRANTED")
                    //show dialog require user turn on GPS, unless can not run app
                    /*AlertDialog.Builder(this)
                            .setMessage(resources.getString(R.string.dialog_require_permission_location_message))
                            .setPositiveButton(resources.getString(R.string.all_ok),DialogInterface.OnItemCheckedChangeListener { dialog, which ->
                                dialog.dismiss()
                                //go to setting permission
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts("package", packageName, null)
                                intent.data = uri
                                startActivityForResult(intent,0x1)
                            })
                            .setNegativeButton(resources.getString(R.string.all_not_ok),DialogInterface.OnItemCheckedChangeListener { dialog, which ->
                                dialog.dismiss()
                                finishAffinity()
                            })
                            .setCancelable(false)
                            .show()*/
                }
            }
            ScanQRFragment.RequestCameraPermissionID -> {
                if (recheckPermissionCamera())
                    scanQRFragment?.grantPermissionCameraSuccess()
            }
        }
    }

    private fun checkPermissionCamera(): Boolean {
        val ACCESS_CAMERA = Manifest.permission.CAMERA
        val checkPermissionCamera = ContextCompat.checkSelfPermission(this, ACCESS_CAMERA)
        val GRANTED = PackageManager.PERMISSION_GRANTED
        val dialogRegrantCamera = ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_CAMERA)

        if (checkPermissionCamera != GRANTED) {
            if (dialogRegrantCamera) {
                //show dialog explain if user not access permission at first
                AlertDialog.Builder(this)
                        .setView(R.layout.dialog_explan_permission_camera)
                        .setPositiveButton(getString(R.string.all_ok)) { dialogInterface, i ->
                            //Prompt the user once explanation has been shown
                            dialogInterface.dismiss()
                            ActivityCompat.requestPermissions(this,
                                    arrayOf(ACCESS_CAMERA),
                                    ScanQRFragment.RequestCameraPermissionID)
                        }
                        .setCancelable(false)
                        .create()
                        .show()
            } else {
                ActivityCompat.requestPermissions(this,
                        arrayOf(ACCESS_CAMERA),
                        ScanQRFragment.RequestCameraPermissionID)
            }
            return false
        }
        return true
    }

    private fun recheckPermissionCamera(): Boolean {
        val ACCESS_CAMERA = Manifest.permission.CAMERA
        val checkPermissionCamera = ContextCompat.checkSelfPermission(this, ACCESS_CAMERA)
        val GRANTED = PackageManager.PERMISSION_GRANTED
        val dialogRegrantCamera = ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_CAMERA)

        if (checkPermissionCamera != GRANTED) {
            if (!dialogRegrantCamera) {
                AlertDialog.Builder(this)
                        .setView(R.layout.dialog_explan_permission_camera)
                        .setPositiveButton(getString(R.string.all_ok)) { dialogInterface, i ->
                            //Prompt the user once explanation has been shown
                            dialogInterface.dismiss()
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", this.packageName, null)
                            intent.data = uri
                            startActivityForResult(intent, ScanQRFragment.RequestCameraPermissionID)
                        }
                        .setCancelable(false)
                        .create()
                        .show()
            } else {
                checkPermissionCamera()
            }
            return false
        }
        return true
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity()
        }
        this.doubleBackToExitPressedOnce = true
        GlobalHelper.showMessage(this, resources.getString(R.string.press_once_to_exit), true)
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }


    override fun onDestroy() {
        super.onDestroy()
        Constraint.myLocation = null
        googleApiClient?.disconnect()
    }

    private fun showSpotLight() {

        val spotLightScan = createSpotLight(layoutScan, resources.getString(R.string.spotlight_qr_title), resources.getString(R.string.spotlight_qr_content))

        Spotlight.with(this)
                .setOverlayColor(R.color.colorMistApp)
                .setDuration(200L)
                .setAnimation(DecelerateInterpolator(5f))
                //.setTargets(simpleTarget1,simpleTarget2,simpleTarget3)
                .setTargets(spotLightScan)
                .setClosedOnTouchedOutside(true)
                .setOnSpotlightStateListener(object : OnSpotlightStateChangedListener {
                    override fun onStarted() {

                    }

                    override fun onEnded() {
                        getSharedPreferences(Constraint.FIRST_USE, Context.MODE_PRIVATE)
                                .edit()
                                .putBoolean(Constraint.INFO_USER_IS_FIRST_USE, false)
                                .apply()
                    }
                })
                .start()
    }

    fun createSpotLight(view: View, title: String, description: String): SimpleTarget {
        val locationView = IntArray(2)
        view.getLocationInWindow(locationView)
        val width = view.width
        val height = view.height
        val radius = Math.sqrt(width.toDouble() * width + height * height) / 2

        //imgLauncher.getLocationOnScreen(location1)

        val target = SimpleTarget.Builder(this)
                .setPoint(locationView[0].toFloat() + width / 2, locationView[1].toFloat() + height / 2)
                .setShape(Circle(radius.toFloat()))
                .setTitle(title)
                .setDuration(200L)
                .setDescription(description)
                .setOnSpotlightStartedListener(object : OnTargetStateChangedListener<SimpleTarget> {
                    override fun onStarted(target: SimpleTarget) {
                        // do something
                    }

                    override fun onEnded(target: SimpleTarget) {
                        // do something
                    }
                })
                .build()

        return target
    }

    fun createSpotLight(x: Int, y: Int, r: Int, title: String, description: String): SimpleTarget {
        //imgLauncher.getLocationOnScreen(location1)

        return SimpleTarget.Builder(this)
                .setPoint(x.toFloat(), y.toFloat())
                .setShape(Circle(r.toFloat()))
                .setTitle(title)
                .setDuration(200L)
                .setDescription(description)
                .setOnSpotlightStartedListener(object : OnTargetStateChangedListener<SimpleTarget> {
                    override fun onStarted(target: SimpleTarget) {
                        // do something
                    }

                    override fun onEnded(target: SimpleTarget) {
                        // do something
                    }
                })
                .build()
    }

    private fun isFirstUse(): Boolean {
        val result = getSharedPreferences(Constraint.FIRST_USE, Context.MODE_PRIVATE)
                .getBoolean(Constraint.INFO_USER_IS_FIRST_USE, true)
        return result
    }

    override fun onConnected(p0: Bundle?) {
        Log.d("****Location", "Connected")
        //Check permission
        val permissionLocation = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
        val listPermissionsNeeded = ArrayList<String>()
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS)
            }
        } else {
            //call google to get location
            val builder = LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest!!)
                    .setAlwaysShow(true)
            val resultPending = LocationServices.SettingsApi.checkLocationSettings(
                    googleApiClient,
                    builder.build()
            )
            Log.d("****Location", "RequestLocation")
            resultPending.setResultCallback(this)
            //requestLocationUpdate()
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        //nothing to do
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        //nothing to do
    }

    override fun onLocationChanged(p0: Location?) {
        Log.d("****LocationChange", "Co do location change")

        if (p0 != null){
            Constraint.myLocation = LatLng(p0.latitude, p0.longitude)
            //set value for province adjust is my location
            AdjustFragment.locationProvince = LatLng(p0!!.latitude, p0.longitude)
            AdjustFragment.provinceChoosed = resources.getString(R.string.all_your_location)

            getSharedPreferences(Constraint.SHARE_PRE_INFO_USER, Context.MODE_PRIVATE)
                    .edit()
                    .putString(Constraint.INFO_USER_LOCATION, "${p0.latitude};${p0.longitude}")
                    .apply()
            if (fragmentIsShowing == TAB_MAP_TAG) {
                Log.d("****LoadStore", "From activity parent")
                (fragmentNew as MapFragment).updateDataWhenLocationChange()
            }
        }
    }

    override fun onResult(p0: LocationSettingsResult) {
        when (p0.status.statusCode) {
            LocationSettingsStatusCodes.SUCCESS -> setupRealTimeLocation()
            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                //  GPS disabled show the user a dialog to turn it on
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    //detect from activityHome, can't listen on this fragment
                    //so after get result from homeActivity-> call thisFragment.getMylocation

                    p0.status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS_GPS)

                } catch (e: Exception) {
                    //failed to show dialog
                }
            }

            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

            }
        }
    }

    private fun setUpGClient() {
        googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        googleApiClient!!.connect()
    }

    private fun setUpLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest!!.interval = 3000
        locationRequest!!.fastestInterval = 3000
        locationRequest!!.smallestDisplacement = 300.toFloat()
    }

    private fun setupRealTimeLocation() {
        if (googleApiClient!!.isConnected) {
            requestLocationUpdate()
        } else {
            Handler().postDelayed({
                if (googleApiClient!!.isConnected)
                    requestLocationUpdate()
            }, 5000)
        }
    }

    private fun requestLocationUpdate() {
        Log.d("****Location", "Request location update")
        val permissionLocation = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            //realtime location
            /*val locationRequest = LocationRequest()
            *//*locationRequest!!.interval = 3000
            locationRequest!!.fastestInterval = 3000*//*
            locationRequest.smallestDisplacement = 300.toFloat()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY*/
            val builder = LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest!!)
            builder.setAlwaysShow(true)

            LocationServices.FusedLocationApi
                    .requestLocationUpdates(googleApiClient, locationRequest, this)

        }
    }

}
