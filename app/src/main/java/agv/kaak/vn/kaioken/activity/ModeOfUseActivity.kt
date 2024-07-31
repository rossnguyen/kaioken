package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.dialog.DialogRequireUpdate
import agv.kaak.vn.kaioken.entity.CheckVersionResult
import agv.kaak.vn.kaioken.entity.define.AppTypeId
import agv.kaak.vn.kaioken.entity.result.LoginAnonymousResult
import agv.kaak.vn.kaioken.entity.result.LoginResult
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.CheckTokenContract
import agv.kaak.vn.kaioken.mvp.contract.LoginContract
import agv.kaak.vn.kaioken.mvp.presenter.CheckTokenPresenter
import agv.kaak.vn.kaioken.mvp.presenter.LoginPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import agv.kaak.vn.kaioken.utils.NetworkReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log

class ModeOfUseActivity : BaseActivity(), LoginContract.View, CheckTokenContract.View, NetworkReceiver.OnChangeNetwork {
    private val loginPresenter = LoginPresenter(this)
    private val checkTokenPresenter = CheckTokenPresenter(this)
    var hadOldSession = false

    val networkReceiver = NetworkReceiver(this)
    var isRegisterNetworkReceiver = false
    var dialogRequireUpdate: DialogRequireUpdate? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode_of_use)
        addEvent()

        if (networkValid()) {
            hadOldSession = checkOldSession()
            getRetrofitAndErrorMap()

            val versionName = this.packageManager
                    .getPackageInfo(packageName, 0)
                    .versionName
            loginPresenter.checkVersionLogin(versionName, AppTypeId.ANDROID_KAIOKEN)
        } else {
            //register broadcast receive network
            isRegisterNetworkReceiver = true
            val filter = IntentFilter()
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
            super.registerReceiver(networkReceiver, filter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRegisterNetworkReceiver)
            unregisterReceiver(networkReceiver)
    }

    override fun onBackPressed() {
        //nothing to do
    }

    fun addEvent() {}

    override fun onConnectSocketSuccess() {
        goToHomeActivity()
    }

    override fun onConnectSocketFail() {
        GlobalHelper.showMessage(applicationContext, "Socket connect fail", false)
        if (networkValid()) {
            GlobalHelper.showMessage(applicationContext, resources.getString(R.string.mode_use_session_invalid_message), false)
            goToLoginActivity()
        } else {
            //register broadcast receive network
            if (!isRegisterNetworkReceiver) {
                isRegisterNetworkReceiver = true
                val filter = IntentFilter()
                filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
                super.registerReceiver(networkReceiver, filter)
                startActivity(Intent(this, ModeOfUseActivity::class.java))
            }
        }

    }

    override fun loginAnonymousSuccess(data: LoginAnonymousResult) {
        //nothing to do
    }

    override fun loginAnonymousFail(message: String?) {
        //nothing to do
    }

    override fun checkTokenValid() {
        getRetrofitAndSocket()
    }

    override fun checkTokenInValid(msg: String?) {
        Log.d("${Constraint.TAG_LOG}TokenExpire", "${Constraint.uid} - ${Constraint.sid}")
        GlobalHelper.showMessage(applicationContext, resources.getString(R.string.mode_use_session_invalid_message), false)
        goToLoginActivity()
    }

    override fun checkTokenError(msg: String?) {
        Log.d("${Constraint.TAG_LOG}TokenError", msg)
        if (GlobalHelper.networkIsConnected(this))
            GlobalHelper.showMessage(applicationContext, resources.getString(R.string.all_error_global), false)
        goToLoginActivity()
    }

    override fun versionValid() {
        if (hadOldSession) {
            checkTokenPresenter.checkToken(Constraint.uid!!, Constraint.sid!!)
        } else
            goToLoginActivity()
    }

    override fun versionInvalid(newVersion: CheckVersionResult) {
        showDialogRequireUpdate(newVersion)
    }

    override fun checkVersionFail(msg: String?) {
        if (msg == null || msg.isEmpty())
            GlobalHelper.showMessage(applicationContext, resources.getString(R.string.all_error_global), true)
        else
            GlobalHelper.showMessage(applicationContext, msg, true)
    }

    override fun loginSuccess(infoUser: LoginResult) {
        //nothing to do
    }

    override fun loginFail(message: String?) {
        //nothing to do
    }

    override fun updateCloudMessagingTokenSuccess() {
        //nothing to do
    }

    override fun updateCloudMessagingTokenFailed(msg: String?) {
        //nothing to do
    }

    private fun showDialogRequireUpdate(detailUpdate: CheckVersionResult) {
        if (dialogRequireUpdate == null) {
            dialogRequireUpdate = DialogRequireUpdate(this, detailUpdate, object : DialogRequireUpdate.OnRequireUpdateResult {
                override fun onUpdateClick() {
                    val appPackageName = packageName // getPackageName() from Context or Activity object
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
                    } catch (except: android.content.ActivityNotFoundException) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                    }
                }

                override fun onSkipClick() {
                    if (hadOldSession)
                        goToHomeActivity()
                    else
                        goToLoginActivity()
                }
            })
            dialogRequireUpdate!!.setCancelable(false)
        }


        if (!dialogRequireUpdate!!.isShowing)
            dialogRequireUpdate?.show()
    }

    private fun checkOldSession(): Boolean {
        //getSharedPreferences(Constraint.SHARE_PRE_INFO_USER,Context.MODE_PRIVATE).edit().clear().apply()
        val sharePreferencesInfoUser = getSharedPreferences(Constraint.SHARE_PRE_INFO_USER, Context.MODE_PRIVATE)
        val uid = sharePreferencesInfoUser.getString(Constraint.INFO_USER_ID_KEY, "-1")
        val sid = sharePreferencesInfoUser.getString(Constraint.INFO_USER_SID_KEY, "-1")
        val phoneUser = sharePreferencesInfoUser.getString(Constraint.INFO_USER_PHONE_KEY, "")
        val nameUser = sharePreferencesInfoUser.getString(Constraint.INFO_USER_NAME_KEY, "")
        val loginType = sharePreferencesInfoUser.getInt(Constraint.INFO_USER_LOGIN_TYPE, 0)
        val locationHadSent = sharePreferencesInfoUser.getBoolean(Constraint.INFO_USER_LOCATION_HAD_SEND, true)
        Constraint.uid = uid
        Constraint.sid = sid
        Constraint.loginType = loginType
        Constraint.phoneUser = phoneUser
        Constraint.nameUser = nameUser
        Constraint.locationHadSent = locationHadSent

        if (uid == "-1")
            return false
        return true
    }

    private fun networkValid(): Boolean {
        if (GlobalHelper.networkIsConnected(this)) {
            return true
        }
        return false
    }

    private fun goToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, EnterPhoneNumberActivity::class.java)
        startActivity(intent)
    }

    override fun onNetworkConnected() {
        if (networkValid()) {
            hadOldSession = checkOldSession()
            getRetrofitAndErrorMap()

            val versionName = this.packageManager
                    .getPackageInfo(packageName, 0)
                    .versionName
            loginPresenter.checkVersionLogin(versionName, AppTypeId.ANDROID_KAIOKEN)
        }
    }

    override fun onNetworkDisconnected() {
        //nothing to do
    }
}
