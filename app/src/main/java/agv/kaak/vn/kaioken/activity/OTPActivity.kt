package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.CheckVersionResult
import agv.kaak.vn.kaioken.entity.define.DeviceType
import agv.kaak.vn.kaioken.entity.response.RegisterAccountResponse
import agv.kaak.vn.kaioken.entity.result.LoginAnonymousResult
import agv.kaak.vn.kaioken.entity.result.LoginResult
import agv.kaak.vn.kaioken.entity.result.UserInfo
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.ForgotPasswordContract
import agv.kaak.vn.kaioken.mvp.contract.LoginContract
import agv.kaak.vn.kaioken.mvp.contract.RegisterAccountContract
import agv.kaak.vn.kaioken.mvp.contract.UserContract
import agv.kaak.vn.kaioken.mvp.presenter.ForgotPasswordPresenter
import agv.kaak.vn.kaioken.mvp.presenter.LoginPresenter
import agv.kaak.vn.kaioken.mvp.presenter.RegisterAccountPresenter
import agv.kaak.vn.kaioken.mvp.presenter.UserPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.jaredrummler.android.device.DeviceName
import kotlinx.android.synthetic.main.activity_otp.*
import java.util.concurrent.TimeUnit

class OTPActivity : BaseActivity(), View.OnClickListener,
        RegisterAccountContract.View,
        ForgotPasswordContract.View,
        LoginContract.View,
        UserContract.View {

    private lateinit var phone: String
    private var token: String? = ""
    private var displayName: String? = ""
    private var password: String? = ""
    private var mAuth = FirebaseAuth.getInstance()
    private var credential: PhoneAuthCredential? = null

    private val presenterLogin = LoginPresenter(this)
    private val presenterRegisterAccount = RegisterAccountPresenter(this)
    private val presenterForgotPassword = ForgotPasswordPresenter(this)
    private val presenterUser = UserPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        getData()
        setCountdownTimerForTryAgainButton()
        addEvent()
    }

    fun getData() {
        val bundle = intent.getBundleExtra(Constraint.DATA_SEND)
        phone = bundle.getString("PHONE_NUMBER")
        token = bundle.getString("TOKEN_OTP")
        displayName = bundle.getString("DISPLAY_NAME")
        password = bundle.getString("PASSWORD")
        credential = bundle.getParcelable("CREDENTIAL")

        if (credential != null) {
            confirmOTP()
        }
    }

    fun addEvent() {
        btnContinue.setOnClickListener(this)
        btnTryAgain.setOnClickListener(this)
        btnSaveNewPassword.setOnClickListener(this)

        edtOTP1.addTextChangedListener(OPTTextWatcher(edtOTP2, edtOTP1))
        edtOTP2.addTextChangedListener(OPTTextWatcher(edtOTP3, edtOTP1))
        edtOTP3.addTextChangedListener(OPTTextWatcher(edtOTP4, edtOTP2))
        edtOTP4.addTextChangedListener(OPTTextWatcher(edtOTP5, edtOTP3))
        edtOTP5.addTextChangedListener(OPTTextWatcher(edtOTP6, edtOTP4))
        edtOTP6.addTextChangedListener(OPTTextWatcher(edtOTP6, edtOTP5))

        etNewPassword?.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_DOWN) {
                //if click at drawable
                if (event.rawX >= etNewPassword.right - etNewPassword.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    //if type is password: show password
                    //else hide password
                    if (etNewPassword.transformationMethod != null) {
                        etNewPassword.transformationMethod = null
                    } else {
                        etNewPassword.transformationMethod = PasswordTransformationMethod()
                    }
                    //move cusor to end
                    etNewPassword.setSelection(etNewPassword.length())
                }
            }
            false
        }

    }

    override fun onConnectSocketSuccess() {
        //nothing to do
        goToHomeActivity()
    }

    override fun onConnectSocketFail() {
        //nothing to do
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnContinue -> {
                if (GlobalHelper.networkIsConnected(this))
                    confirmOTP()
            }
            R.id.btnTryAgain -> {
                if (GlobalHelper.networkIsConnected(this)) {
                    setCountdownTimerForTryAgainButton()
                    tryToSendOTPAgain()
                }
            }

            R.id.btnSaveNewPassword -> {
                val inputOTP = "${edtOTP1.text}${edtOTP2.text}${edtOTP3.text}${edtOTP4.text}${edtOTP5.text}${edtOTP6.text}"
                val newPassword = etNewPassword.text.toString().trim()

                showLoading()
                presenterUser.changePassword(inputOTP, newPassword, newPassword)
            }
        }
    }

    override fun registerAccountSuccess(registerAccountResponse: RegisterAccountResponse) {
        hideLoading()

        val apiLevel = Build.VERSION.SDK
        val device = Build.DEVICE
        val model = Build.MODEL
        val product = Build.PRODUCT
        val system = System.getProperty("os.version")
        val userAgent = apiLevel + "_" + device + "_" + model + "_" + product + "_" + system
        val deviceName = DeviceName.getDeviceName()

        showLoading()
        presenterLogin.login(phone, password!!, 0, deviceName, DeviceType.TYPE_ANDROID, userAgent)
    }

    override fun registerAccountFailed(message: String) {
        hideLoading()
        showMessage(message)
    }

    override fun phoneNotExist() {
        //nothing to do
    }

    override fun phoneExist(userInfo: UserInfo) {
        //nothing to do
    }

    override fun checkPhoneExistFailed(msg: String?) {
        //nothing to do
    }

    override fun forgotPasswordSuccess(loginResult: LoginResult) {
        //  update cloud messaging token when login successfully
        /*val sharedPreferences = getSharedPreferences(Constraint.SHARE_PRE_CLOUD_MESSAGING, MODE_PRIVATE)
        val token = sharedPreferences.getString(Constraint.SHARED_PRE_CLOUD_MESSAGING_TOKEN, "")
        Log.d("${Constraint.TAG_LOG}tokenCloud",token)
        showLoading()
        presenterLogin.updateCloudMessagingToken(token)*/

        Constraint.uid = loginResult.id.toString()
        Constraint.sid = loginResult.sid
        /*Constraint.loginType = 1
        Constraint.avatar = loginResult.avatar!!
        Constraint.nameUser = loginResult.name!!
        Constraint.phoneUser = loginResult.phone!!.replace("+84", "0")*/

        /*val pre = getSharedPreferences(Constraint.SHARE_PRE_INFO_USER, MODE_PRIVATE)
        val edit = pre.edit()
        edit.putString(Constraint.INFO_USER_ID_KEY, loginResult.id.toString())
        edit.putString(Constraint.INFO_USER_EMAIL_KEY, loginResult.email)
        edit.putString(Constraint.INFO_USER_NAME_KEY, loginResult.name)
        edit.putString(Constraint.INFO_USER_SID_KEY, loginResult.sid)
        edit.putString(Constraint.INFO_USER_AVATAR_KEY, loginResult.avatar)
        edit.putString(Constraint.INFO_USER_PHONE_KEY, loginResult.phone!!.replace("+84", "0"))
        edit.putInt(Constraint.INFO_USER_LOGIN_TYPE, 1)
        edit.apply()

        if (Constraint.BASE_SOCKET != null) {
            Constraint.BASE_SOCKET?.disconnect()
            Constraint.BASE_SOCKET = null
        }*/
        getRetrofitAndErrorMap()

        showLayoutEnterNewPassword()
        hideKeyboard(edtOTP6)
    }

    override fun forgotPasswordFailed(msg: String?) {
        hideLoading()
        showMessage(msg!!)
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

    override fun checkVersionFail(message: String?) {
        //nothing to do
    }

    override fun loginSuccess(infoUser: LoginResult) {
        hideLoading()

        //  update cloud messaging token when login successfully
        val sharedPreferences = getSharedPreferences(Constraint.SHARE_PRE_CLOUD_MESSAGING, MODE_PRIVATE)
        val token = sharedPreferences.getString(Constraint.SHARED_PRE_CLOUD_MESSAGING_TOKEN, null)
        showLoading()
        presenterLogin.updateCloudMessagingToken(token)

        Constraint.uid = infoUser.id.toString()
        Constraint.sid = infoUser.sid
        Constraint.loginType = 1
        Constraint.locationHadSent = false

        val pre = getSharedPreferences(Constraint.SHARE_PRE_INFO_USER, MODE_PRIVATE)
        val edit = pre.edit()
        edit.putString(Constraint.INFO_USER_ID_KEY, infoUser.id.toString())
        edit.putString(Constraint.INFO_USER_EMAIL_KEY, infoUser.email)
        edit.putString(Constraint.INFO_USER_NAME_KEY, infoUser.name)
        edit.putString(Constraint.INFO_USER_SID_KEY, infoUser.sid)
        edit.putString(Constraint.INFO_USER_AVATAR_KEY, infoUser.avatar)
        edit.putInt(Constraint.INFO_USER_LOGIN_TYPE, 1)
        edit.putBoolean(Constraint.INFO_USER_LOCATION_HAD_SEND, false)
        edit.apply()

        if (Constraint.BASE_SOCKET != null) {
            Constraint.BASE_SOCKET?.disconnect()
            Constraint.BASE_SOCKET = null
        }

        getRetrofitAndSocket()
    }

    override fun loginFail(message: String?) {
        hideLoading()
        showMessage(resources.getString(R.string.login_fail_message))
    }

    override fun updateCloudMessagingTokenSuccess() {
        hideLoading()
    }

    override fun updateCloudMessagingTokenFailed(msg: String?) {
        hideLoading()
    }

    override fun getUserInfoSuccess(userInfo: UserInfo) {
        //nothing to do
    }

    override fun getUserInfoFail(msg: String?) {
        //nothing to do
    }

    override fun updateUserInfoSuccess() {
        //nothing to do
    }

    override fun updateUserInfoFail(msg: String?) {
        //nothing to do
    }

    override fun changePasswordSuccess() {
        val newPassword = etNewPassword.text.toString().trim()
        Log.d("${Constraint.TAG_LOG}newpass", newPassword)
        //showMessage(resources.getString(R.string.login_this_otp_is_new_password))
        val apiLevel = Build.VERSION.SDK
        val device = Build.DEVICE
        val model = Build.MODEL
        val product = Build.PRODUCT
        val system = System.getProperty("os.version")
        val userAgent = apiLevel + "_" + device + "_" + model + "_" + product + "_" + system
        val deviceName = DeviceName.getDeviceName()
        presenterLogin.login(phone, newPassword, 0, deviceName, DeviceType.TYPE_ANDROID, userAgent)
    }

    override fun changePasswordFail(msg: String?) {
        hideLoading()
        showMessage(resources.getString(R.string.update_profile_update_password_fail))
    }

    private fun hideLoading() {
        layoutLoading.visibility = View.GONE
    }

    private fun showLoading() {
        layoutLoading.visibility = View.VISIBLE
    }

    private fun goToHomeActivity() {
        //go to home activity
        startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun tryToSendOTPAgain() {
        val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                hideLoading()
                Log.e("****verifiComplete", "$p0")
                credential = p0
                confirmOTP()
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
                Log.e("****verifiCodeSent", "$p0")
                hideLoading()
                credential = null
                token = p0
                showMessage(resources.getString(R.string.all_sented))
            }
        }

        showLoading()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                callback)        // OnVerificationStateChangedCallbacks
    }

    private fun confirmOTP() {
        if (credential == null) {
            val inputOTP = "${edtOTP1.text}${edtOTP2.text}${edtOTP3.text}${edtOTP4.text}${edtOTP5.text}${edtOTP6.text}"
            //check valid OTP
            if (inputOTP.length < 6) {
                showMessage(getString(R.string.register_invalid_otp_cause_length))
                return
            }
            //check valid token
            if (token == null) {
                showMessage(getString(R.string.all_error_global))
                return
            }

            credential = PhoneAuthProvider.getCredential(token!!, inputOTP)
        }

        showLoading()
        signInWithPhoneAuthCredential(credential!!)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        hideLoading()
                        // Sign in success, update UI with the signed-in user's information
                        task.result.user.getIdToken(true).addOnCompleteListener {
                            if (it.isSuccessful) {
                                if (Constraint.IS_CHANGE_PASSWORD)
                                    changePassword(it, task)
                                else
                                    registerAccount(it, task)

                            } else showMessage(resources.getString(R.string.something_went_wrong))
                        }
                    } else {
                        hideLoading()
                        // Sign in failed, display drawable_gradient_black message and update the UI
                        Log.e("****signInFail", "signInWithCredential: failure cause ${task.exception}")
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            showMessage(resources.getString(R.string.register_invalid_otp))
                        } else
                            showMessage(resources.getString(R.string.all_error_global))
                    }
                }
    }

    private fun changePassword(it: Task<GetTokenResult>, task: Task<AuthResult>) {
        val tokenGoogleReceived = it.result.token
        val inputOTP = "${edtOTP1.text}${edtOTP2.text}${edtOTP3.text}${edtOTP4.text}${edtOTP5.text}${edtOTP6.text}"

        showLoading()
        Log.d("${Constraint.TAG_LOG}TokenGoogle", tokenGoogleReceived)
        presenterForgotPassword.forgotPassword(task.result.user.phoneNumber!!, inputOTP, tokenGoogleReceived!!)
    }

    private fun showLayoutEnterNewPassword() {
        hideLoading()
        layoutInputCode.visibility = View.GONE
        layoutNewPassword.visibility = View.VISIBLE
    }

    private fun registerAccount(it: Task<GetTokenResult>, task: Task<AuthResult>) {
        val token = it.result.token
        val apiLevel = Build.VERSION.SDK
        val device = Build.DEVICE
        val model = Build.MODEL
        val product = Build.PRODUCT
        val system = System.getProperty("os.version")
        val detail = apiLevel + "_" + device + "_" + model + "_" + product + "_" + system
        val deviceName = DeviceName.getDeviceName()

        showLoading()

        presenterRegisterAccount.registerAccount(task.result.user.phoneNumber!!, token!!,
                DeviceType.TYPE_ANDROID,
                deviceName, detail, displayName!!, password!!, password!!)
    }

    private fun setCountdownTimerForTryAgainButton() {
        object : CountDownTimer(60000L, 1000L) {
            override fun onFinish() {
                btnTryAgain?.isEnabled = true
                btnTryAgain?.text = resources.getString(R.string.all_try_again)
            }

            override fun onTick(millisUntilFinished: Long) {
                btnTryAgain?.isEnabled = false
                btnTryAgain?.text = resources.getString(R.string.register_try_again_after_x_seconds, millisUntilFinished / 1000)
            }
        }.start()
    }

    internal class OPTTextWatcher(private val nextEditText: EditText, private val previousEditText: EditText?) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val text = s?.toString()
            if (text?.length == 1)
                nextEditText.requestFocus()
            else if (text?.length == 0)
                previousEditText?.requestFocus()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

}
