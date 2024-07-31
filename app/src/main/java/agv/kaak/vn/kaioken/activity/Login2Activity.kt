package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.CheckVersionResult
import agv.kaak.vn.kaioken.entity.define.DeviceType
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.entity.result.LoginAnonymousResult
import agv.kaak.vn.kaioken.entity.result.LoginResult
import agv.kaak.vn.kaioken.entity.result.UserInfo
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.helper.GlobalHelper.Companion.getPhoneNumber
import agv.kaak.vn.kaioken.helper.ImageHelper
import agv.kaak.vn.kaioken.mvp.contract.LoginContract
import agv.kaak.vn.kaioken.mvp.presenter.LoginPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.jaredrummler.android.device.DeviceName
import kotlinx.android.synthetic.main.activity_login2.*
import java.util.concurrent.TimeUnit

class Login2Activity : BaseActivity(), View.OnClickListener, LoginContract.View {

    private val presenter = LoginPresenter(this)
    private var userInfo: UserInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)
        getData()
        loadData()
        addEvent()
        ViewCompat.setElevation(btnLogin, resources.getDimension(R.dimen.all_masterial_elevation_raised_button_rest))
    }

    private fun getData() {
        userInfo = intent.getSerializableExtra(Constraint.DATA_SEND) as UserInfo
    }

    private fun loadData() {
        if (userInfo != null) {
            ImageHelper.loadImage(this, imgAvatar, userInfo!!.linkAvarta, PlaceHolderType.AVATAR)
            tvName.text = userInfo!!.name
        }
    }

    private fun addEvent() {
        //  show and hide password when click at drawable
        etPassWord?.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_DOWN) {
                //if click at drawable
                if (event.rawX >= etPassWord.right - etPassWord.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    //if type is password: show password
                    //else hide password
                    if (etPassWord.transformationMethod != null) {
                        etPassWord.transformationMethod = null
                    } else {
                        etPassWord.transformationMethod = PasswordTransformationMethod()
                    }
                    //move cusor to end
                    etPassWord.setSelection(etPassWord.length())
                }
            }

            false
        }
        //set onclick listener for all control
        tvForgotPassword?.setOnClickListener(this)
        btnLogin?.setOnClickListener(this)
        ibtnBack.setOnClickListener(this)
    }

    override fun onConnectSocketSuccess() {
        //nothing to do
        goToHomeActivity()
    }

    override fun onConnectSocketFail() {
        //nothing to do
    }

    override fun loginAnonymousSuccess(data: LoginAnonymousResult) {
        //nothing to do
    }

    override fun loginAnonymousFail(message: String?) {
        //nothing to do
    }

    override fun versionValid() {
        //nothing to do
    }

    override fun versionInvalid(newVersion: CheckVersionResult) {
        //nothing to do
    }

    override fun checkVersionFail(msg: String?) {
        //nothing to do
    }

    override fun loginSuccess(infoUser: LoginResult) {
        hideLoading()
        //Constraint.IS_RESTART_APPCOMPONENT = true

        //  update cloud messaging token when login successfully
        val sharedPreferences = getSharedPreferences(Constraint.SHARE_PRE_CLOUD_MESSAGING, MODE_PRIVATE)
        val token = sharedPreferences.getString(Constraint.SHARED_PRE_CLOUD_MESSAGING_TOKEN, "")
        Log.d("${Constraint.TAG_LOG}tokenCloud", token)
        showLoading()

        presenter.updateCloudMessagingToken(token)

        Constraint.uid = infoUser.id.toString()
        Constraint.sid = infoUser.sid
        Constraint.loginType = 1
        Constraint.avatar = infoUser.avatar!!
        Constraint.nameUser = infoUser.name!!
        Constraint.phoneUser = infoUser.phone!!.replace("+84", "0")
        Constraint.locationHadSent = false

        val pre = getSharedPreferences(Constraint.SHARE_PRE_INFO_USER, MODE_PRIVATE)
        val edit = pre.edit()
        edit.putString(Constraint.INFO_USER_ID_KEY, infoUser.id.toString())
        edit.putString(Constraint.INFO_USER_EMAIL_KEY, infoUser.email)
        edit.putString(Constraint.INFO_USER_NAME_KEY, infoUser.name)
        edit.putString(Constraint.INFO_USER_SID_KEY, infoUser.sid)
        edit.putString(Constraint.INFO_USER_AVATAR_KEY, infoUser.avatar)
        edit.putString(Constraint.INFO_USER_PHONE_KEY, infoUser.phone!!.replace("+84", "0"))
        edit.putInt(Constraint.INFO_USER_LOGIN_TYPE, 1)
        edit.putBoolean(Constraint.INFO_USER_LOCATION_HAD_SEND, false)
        edit.apply()

        hideLoading()
        if (Constraint.BASE_SOCKET != null) {
            Constraint.BASE_SOCKET?.disconnect()
            Constraint.BASE_SOCKET = null
        }

        getRetrofitAndSocket()
    }

    override fun loginFail(message: String?) {
        hideLoading()
        showMessage(getString(R.string.login_fail_message))
    }

    override fun updateCloudMessagingTokenSuccess() {
        hideLoading()
    }

    override fun updateCloudMessagingTokenFailed(msg: String?) {
        hideLoading()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvForgotPassword -> doGetPassword()
            R.id.btnLogin -> {
                val phoneNumber = getPhoneNumber(userInfo!!.phone!!)
                val password = etPassWord.text.toString()
                if (GlobalHelper.networkIsConnected(this))
                    doLogin(phoneNumber, password)

            }
            R.id.ibtnBack -> onBackPressed()
        }
    }

    fun hideLoading() {
        layoutLoading.visibility = View.GONE
    }

    fun showLoading() {
        layoutLoading.visibility = View.VISIBLE
    }

    private fun doLogin(phoneNumber: String, password: String) {
        val apiLevel = Build.VERSION.SDK
        val device = Build.DEVICE
        val model = Build.MODEL
        val product = Build.PRODUCT
        val system = System.getProperty("os.version")
        val userAgent = apiLevel + "_" + device + "_" + model + "_" + product + "_" + system
        val deviceName = DeviceName.getDeviceName()

        showLoading()
        presenter.login(phoneNumber, password, 0, deviceName, DeviceType.TYPE_ANDROID, userAgent)
    }

    private fun doGetPassword() {
        Constraint.IS_CHANGE_PASSWORD = true
        val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                hideLoading()
                Log.d("****verifiComplete", "$p0")
            }

            override fun onVerificationFailed(p0: FirebaseException?) {
                hideLoading()
                Log.e("****verifiFail", "$p0")
                if (p0 is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.e("****verifiFail", "Invalid request")
                } else if (p0 is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.e("****verifiFail", "The SMS quota for the project has been exceeded")
                }
                showMessage(getString(R.string.something_went_wrong))
            }

            override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                super.onCodeSent(p0, p1)
                Log.d("****verifiCodeSent", "$p0")

                hideLoading()
                showMessage(getString(R.string.all_sented))

                val bundle = Bundle()
                bundle.putString("PHONE_NUMBER", getPhoneNumber(userInfo?.phone!!))
                bundle.putString("TOKEN_OTP", p0)

                val intent = Intent(this@Login2Activity, OTPActivity::class.java)
                intent.putExtra(Constraint.DATA_SEND, bundle)
                startActivity(intent)
                finish()
            }
        }

        showLoading()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                getPhoneNumber(userInfo?.phone!!),        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                callback)     // OnVerificationStateChangedCallbacks
    }

    private fun goToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)

        //  finish current activity
        finish()
    }
}
