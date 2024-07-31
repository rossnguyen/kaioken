package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_register.*
import java.util.concurrent.TimeUnit
import android.text.method.LinkMovementMethod


class RegisterActivity : BaseActivity() {

    private var phoneNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        Constraint.IS_CHANGE_PASSWORD = false
        getData()
        addEvent()
        setupTermAndCondition()
        //setOptionHeaderText()
    }

    private fun getData() {
        phoneNumber = intent.getStringExtra(Constraint.DATA_SEND)
    }

    fun addEvent() {
        val touchToShowPassword = View.OnTouchListener { v, event ->
            val view = v as EditText
            val DRAWABLE_RIGHT = 2
            if (event.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_DOWN) {
                //if click at drawable
                if (event.rawX >= view.right - view.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    //if type is password: show password
                    //else hide password
                    if (view.transformationMethod != null) {
                        view.transformationMethod = null
                    } else {
                        view.transformationMethod = PasswordTransformationMethod()
                    }
                    //move cusor to end
                    view.setSelection(view.length())
                }
            }
            false
        }
        etPassWord?.setOnTouchListener(touchToShowPassword)

        btnRegister.setOnClickListener {
            phoneNumber = getPhoneNumber(phoneNumber)
            val password = etPassWord.text.toString()
            val displayName = edtDisplayName.text.toString()
            if (doCheckValid(password, displayName) and GlobalHelper.networkIsConnected(this))
                sendOTP()
        }

        ibtnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onConnectSocketSuccess() {
        //nothing to do
    }

    override fun onConnectSocketFail() {
        //nothing to do
    }

    private fun sendOTP() {
        val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
                p0!!.smsCode
                hideLoading()
                Log.e("****verifiComplete", "$p0")
                //trường hợp Không cần qua bước kiểm tra code luon

                val bundle = Bundle()
                bundle.putString("PHONE_NUMBER", getPhoneNumber(phoneNumber))
                bundle.putString("DISPLAY_NAME", edtDisplayName.text.toString())
                bundle.putString("PASSWORD", etPassWord.text.toString())
                bundle.putParcelable("CREDENTIAL", p0)
                val intent = Intent(this@RegisterActivity, OTPActivity::class.java)
                intent.putExtra(Constraint.DATA_SEND, bundle)
                startActivity(intent)
            }

            override fun onVerificationFailed(p0: FirebaseException?) {
                hideLoading()
                Log.e("****verifiFail", "$p0")

                if (p0 is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.e("****verifiFail", "onVerificationFailed: Invalid request")
                } else if (p0 is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.e("****verifiFail", "onVerificationFailed: The SMS quota for the project has been exceeded")
                }
                showDialogMessage(null, resources.getString(R.string.register_can_not_send_otp))
            }

            override fun onCodeSent(p0: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                super.onCodeSent(p0, p1)
                hideLoading()

                val bundle = Bundle()
                bundle.putString("PHONE_NUMBER", getPhoneNumber(phoneNumber))
                bundle.putString("TOKEN_OTP", p0)
                bundle.putString("DISPLAY_NAME", edtDisplayName.text.toString())
                bundle.putString("PASSWORD", etPassWord.text.toString())
                val intent = Intent(this@RegisterActivity, OTPActivity::class.java)
                intent.putExtra(Constraint.DATA_SEND, bundle)
                startActivity(intent)
            }
        }

        showLoading()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                getPhoneNumber(phoneNumber),        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                callback)        // OnVerificationStateChangedCallbacks
    }

    fun showLoading() {
        layoutHandling?.visibility = View.VISIBLE
    }

    fun hideLoading() {
        layoutHandling?.visibility = View.GONE
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

    private fun doCheckValid(password: String, displayName: String): Boolean {
        if (displayName.isEmpty()) {
            showDialogMessage(null, resources.getString(R.string.payment_enter_full))

            return false
        }
        if (password.length < 6) {
            showDialogMessage(null, resources.getString(R.string.register_password_must_more_6_characters))
            return false
        }

        //check exist account
        return true
    }

    private fun setupTermAndCondition() {
        tvTermsAndCondition.movementMethod = LinkMovementMethod.getInstance()
    }
}
