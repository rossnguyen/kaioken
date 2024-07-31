package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.fragment.info_store.ImageStoreFragment
import agv.kaak.vn.kaioken.utils.Constraint
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog

class SplashActivity : AppCompatActivity() {
    val MY_PERMISSIONS_REQUEST = 99
    private val CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084

    private val GRANTED = PackageManager.PERMISSION_GRANTED
    var checkPermissionLocation: Int = 0
    var checkPermissionCamera: Int = 0
    private var ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private var ACCESS_CAMERA = Manifest.permission.CAMERA

    private var dialogRegrantLocation: Boolean = false
    private var dialogRegrantCamera: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Constraint.callFromNotification = intent.getBooleanExtra("FROM_NOTIFICATION", false)

        if (checkMultiPermission())
            handleAfterCheckPermission()


    }

    private fun checkMultiPermission(): Boolean {
        checkPermissionLocation = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
        checkPermissionCamera = ContextCompat.checkSelfPermission(this, ACCESS_CAMERA)
        dialogRegrantLocation = ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)
        dialogRegrantCamera = ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_CAMERA)

        if ((checkPermissionLocation != GRANTED) or (checkPermissionCamera != GRANTED)) {
            // Should we show an explanation?
            if (dialogRegrantLocation or dialogRegrantCamera) {
                //show dialog explain if user not access permission at first
                AlertDialog.Builder(this)
                        .setView(R.layout.dialog_explain_permission)
                        .setPositiveButton(getString(R.string.all_ok)) { _, _ ->
                            //Prompt the user once explanation has been shown
                            var listPermission: Array<String> = arrayOf()
                            if (checkPermissionLocation != GRANTED)
                                listPermission += ACCESS_FINE_LOCATION
                            if (checkPermissionCamera != GRANTED)
                                listPermission += ACCESS_CAMERA

                            ActivityCompat.requestPermissions(this,
                                    listPermission,
                                    MY_PERMISSIONS_REQUEST)
                        }
                        .setCancelable(false)
                        .create()
                        .show()
            } else {
                // No explanation needed, we can request the permission.
                var listPermission: Array<String> = arrayOf()
                if (checkPermissionLocation != GRANTED)
                    listPermission += ACCESS_FINE_LOCATION
                if (checkPermissionCamera != GRANTED)
                    listPermission += ACCESS_CAMERA

                ActivityCompat.requestPermissions(this,
                        listPermission,
                        MY_PERMISSIONS_REQUEST)

            }
            return false
        } else {
            return true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST -> {
                showDialogPermission()
            }

        /*CODE_DRAW_OVER_OTHER_APP_PERMISSION -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                //show activity get permission draw all diplay
                //Check if the application has draw over other apps permission or not?
                //This permission is by default available for API<23. But for API > 23
                //you have to ask for the permission in runtime.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                    //If the draw over permission is not available open the settings screen
                    //to grant the permission.
                    AlertDialog.Builder(this)
                            .setTitle(getString(R.string.request_permission))
                            .setMessage(getString(R.string.request_permission_draw_all))
                            .setPositiveButton(getString(R.string.all_ok)) { dialogInterface, i ->
                                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:$packageName"))
                                startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION)
                            }
                            .create()
                            .show()

                }
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }*/
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            showDialogPermission()
        }
    }

    private fun showDialogPermission() {
        checkPermissionLocation = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
        checkPermissionCamera = ContextCompat.checkSelfPermission(this, ACCESS_CAMERA)
        dialogRegrantLocation = ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)
        dialogRegrantCamera = ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_CAMERA)

        if ((checkPermissionLocation != GRANTED) or (checkPermissionCamera != GRANTED)) {
            // Should we show an explanation?
            //if checked never ask again
            if (!dialogRegrantLocation or !dialogRegrantCamera) {
                AlertDialog.Builder(this)
                        .setView(R.layout.dialog_explain_permission)
                        .setPositiveButton(getString(R.string.all_ok)) { _, _ ->
                            //Prompt the user once explanation has been shown
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivityForResult(intent, MY_PERMISSIONS_REQUEST)
                        }
                        .setCancelable(false)
                        .create()
                        .show()

            } else
            //if don't checked never ask again
                checkMultiPermission()
        } else {
            /*
            //Check if the application has draw over other apps permission or not?
            //This permission is by default available for API<23. But for API > 23
            //you have to ask for the permission in runtime.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                //If the draw over permission is not available open the settings screen
                //to grant the permission.
                AlertDialog.Builder(this)
                        .setTitle(getString(R.string.request_permission))
                        .setMessage(getString(R.string.request_permission_draw_all))
                        .setPositiveButton(getString(R.string.all_ok)) { dialogInterface, i ->
                            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:$packageName"))
                            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION)
                        }
                        .create()
                        .show()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }*/

            //if have permission: go to activity login
            handleAfterCheckPermission()
        }
    }

    private fun handleAfterCheckPermission() {
        val intent = Intent(this, ModeOfUseActivity::class.java)
        startActivity(intent)
    }
}

