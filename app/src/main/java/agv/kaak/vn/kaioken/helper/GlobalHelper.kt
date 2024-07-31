package agv.kaak.vn.kaioken.helper

import agv.kaak.vn.kaioken.BuildConfig
import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.activity.CallWaiterActivity
import agv.kaak.vn.kaioken.activity.ModeOfUseActivity
import agv.kaak.vn.kaioken.entity.MenuFood
import agv.kaak.vn.kaioken.fragment.order.ShowDetailFoodFragment
import agv.kaak.vn.kaioken.fragment.order.ShowMenuItemFragment
import agv.kaak.vn.kaioken.mvp.contract.LogoutContract
import agv.kaak.vn.kaioken.mvp.presenter.LogoutPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import android.net.ConnectivityManager
import android.support.v7.app.AlertDialog
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import io.socket.client.Socket
import java.lang.Double
import android.app.ActivityManager
import android.os.CountDownTimer
import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp

class GlobalHelper {
    companion object {
        var dialogCanNotConnectToNetWork: AlertDialog? = null

        fun showMessage(context: Context?, message: String?, durationIsShort: Boolean) {
            if (durationIsShort)
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun doCallWaiter(from: Context) {
            val intent = Intent(from, CallWaiterActivity::class.java)
            from.startActivity(intent)
        }

        fun createProgressDialogHandling(activity: Activity, theme: Int?): ProgressDialog {
            val progressDialog: ProgressDialog
            if (theme != null)
                progressDialog = ProgressDialog(activity, theme)
            else
                progressDialog = ProgressDialog(activity)

            progressDialog.setMessage(activity.resources.getString(R.string.all_message_handling))
            progressDialog.setIcon(R.mipmap.ic_launcher_foreground)
            progressDialog.setCancelable(false)
            return progressDialog
        }

        fun socketIsConnected(activity: Activity, socket: Socket): Boolean {
            if (networkIsConnected(activity))
                if (socket.connected())
                    return true

            AlertDialog.Builder(activity)
                    .setMessage(activity.resources.getString(R.string.all_error_global))
                    .setPositiveButton(activity.resources.getString(R.string.all_ok)) { dialog, which ->
                        dialog.dismiss()
                    }
                    .setCancelable(false)
                    .show()
            return false
        }

        fun ringTheBell(activityParent: Activity) {
            val mp = MediaPlayer.create(activityParent, R.raw.sound_bell)
            //mp = MediaPlayer.create(this, R.raw.build_sucess);
            mp.setOnCompletionListener(MediaPlayer.OnCompletionListener { mp ->
                var mp = mp
                mp!!.reset()
                mp.release()
                mp = null
            })
            mp.start()
        }

        fun showDialogDetailFood(context: Context, foodChoosed: MenuFood) {
            val detailFoodFragment = ShowDetailFoodFragment()
            val bundle = Bundle()
            bundle.putSerializable(ShowMenuItemFragment.DETAIL_FOOD_SEND, foodChoosed)
            detailFoodFragment.arguments = bundle
            detailFoodFragment.show((context as FragmentActivity).supportFragmentManager, "DetailFood")
        }

        fun networkIsConnected(context: Activity): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
            if (isConnected)
                return true
            else {
                if (!Constraint.DIALOG_ERROR_NETWORK_IS_SHOWING) {
                    dialogCanNotConnectToNetWork = AlertDialog.Builder(context)
                            .setMessage(context.resources.getString(R.string.dialog_require_network_message))
                            .setPositiveButton(context.resources.getString(R.string.all_ok),
                                    { dialog, which ->
                                        dialog.dismiss()
                                        Constraint.DIALOG_ERROR_NETWORK_IS_SHOWING = false
                                    })
                            .setCancelable(false)
                            .show()

                    Constraint.DIALOG_ERROR_NETWORK_IS_SHOWING = true
                }

                return false
            }

        }

        fun getLocationFromSharePreference(context: Context): LatLng? {
            val sharedPreferences = context.getSharedPreferences(Constraint.SHARE_PRE_INFO_USER, Context.MODE_PRIVATE)
            val strLatlng = sharedPreferences.getString(Constraint.INFO_USER_LOCATION, "")
            return if (strLatlng !== "") LatLng(Double.parseDouble(strLatlng!!.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]), Double.parseDouble(strLatlng.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])) else null
        }

        fun getPhoneNumber(inputPhoneNumber: String): String {
            var defaultPhoneNumber = inputPhoneNumber
            if (defaultPhoneNumber.isEmpty())
                return ""
            if (defaultPhoneNumber.toCharArray()[0] == '0')
                defaultPhoneNumber = defaultPhoneNumber.replaceFirst("0", "+84")
            if (defaultPhoneNumber.toCharArray()[0] != '0' && defaultPhoneNumber.toCharArray()[0] != '+')
                defaultPhoneNumber = "+84$defaultPhoneNumber"
            return defaultPhoneNumber
        }

        fun doLogout(activity: Activity) {
            val logoutPresenter = LogoutPresenter(object : LogoutContract.View {
                override fun logoutSuccess() {
                    //nothing to do
                    Log.d("${Constraint.TAG_LOG}Logout", "success")

                    clearShareReference(activity)

                    Constraint.BASE_SOCKET?.disconnect()

                    activity.startActivity(Intent(activity, ModeOfUseActivity::class.java))
                }

                override fun logoutFail(msg: String?) {
                    Log.d("${Constraint.TAG_LOG}Logout", "fail $msg")

                    //clear share reference
                    activity.getSharedPreferences(Constraint.SHARE_PRE_INFO_USER, Context.MODE_PRIVATE)
                            .edit()
                            .clear()
                            .apply()
                    //clear constraint
                    Constraint.uid = ""
                    Constraint.sid = ""
                    Constraint.loginType = 0
                    Constraint.ID_STORE_ORDERING = -1
                    Constraint.ID_TABLE_ORDERING = -1
                    Constraint.NAME_STORE_ORDERING = ""
                    Constraint.ADDRESS_STORE_ORDERING = ""
                    Constraint.NAME_CUSTOMER = ""
                    Constraint.TYPE_USE = 3

                    Constraint.BASE_SOCKET?.disconnect()

                    activity.startActivity(Intent(activity, ModeOfUseActivity::class.java))
                }
            })
            logoutPresenter.logout()
        }

        fun clearShareReference(activity: Activity){
            //clear share reference
            activity.getSharedPreferences(Constraint.SHARE_PRE_INFO_USER, Context.MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply()
            //clear constraint
            Constraint.uid = "-1"
            Constraint.sid = ""
            Constraint.loginType = 0
            Constraint.ID_STORE_ORDERING = -1
            Constraint.ID_TABLE_ORDERING = -1
            Constraint.NAME_STORE_ORDERING = ""
            Constraint.ADDRESS_STORE_ORDERING = ""
            Constraint.NAME_CUSTOMER = ""
            Constraint.TYPE_USE = 3
        }

        fun logE(TAG: String, message: String?) {
            if (message != null)
                Log.e("${Constraint.TAG_LOG}$TAG", message)
        }

        fun logD(TAG: String, message: String?) {
            if (message != null)
                Log.e("${Constraint.TAG_LOG}$TAG", message)
        }

        fun gpsIsOn(activity: Activity): Boolean {
            val gpsIsConnected = (activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (!gpsIsConnected) {
                AlertDialog.Builder(activity)
                        .setMessage(activity.resources.getString(R.string.dialog_require_permission_location_message))
                        .setPositiveButton(activity.resources.getString(R.string.all_known)) { dialog, _ ->
                            dialog.dismiss()
                            //go to setting permission
                        }
                        .setCancelable(false)
                        .show()
                return false
            }
            return true
        }

        fun isAppRunning(context: Context, packageName: String): Boolean {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val procInfos = activityManager.runningAppProcesses
            if (procInfos != null) {
                for (processInfo in procInfos) {
                    if (processInfo.processName == packageName) {
                        return true
                    }
                }
            }
            return false
        }

        fun getSignature(): String {
            val timeStamp = Timestamp(System.currentTimeMillis()).time.toString()
            val key = "${BuildConfig.PhucDepTraiKEY}$timeStamp"

            val md = MessageDigest.getInstance("MD5");
            md.reset()
            md.update(key.toByteArray())
            val digest = md.digest()
            val bigInt = BigInteger(1, digest)
            var hashText = bigInt.toString(16)
            while (hashText.length < 32)
                hashText = "0" + hashText


            val signature = "$hashText $timeStamp android"
            return signature
        }

        fun getHeaders(): HashMap<String, String> {
            val headers: HashMap<String, String> = HashMap()
            headers.put("uid", Constraint.uid.toString())
            headers.put("sid", Constraint.sid!!)
            headers.put("x-kaak-signature", getSignature())
            return headers
        }

        fun getHeadersForImage(): HashMap<String, String> {
            val headers: HashMap<String, String> = HashMap()
            headers.put("uid", Constraint.uid.toString())
            headers.put("sid", Constraint.sid!!)
            headers.put("x-kaak-signature", getSignature())
            headers.put("x-kaak-version","2.0")
            return headers
        }
    }
}