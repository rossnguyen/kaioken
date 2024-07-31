package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.helper.GlobalHelper.Companion.getPhoneNumber
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_forgot_password.*
import java.util.concurrent.TimeUnit

class ForgotPasswordActivity : BaseActivity() {
    var phoneNumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        Constraint.IS_CHANGE_PASSWORD = true
        getData()
        addEvent()
    }

    private fun getData() {
        phoneNumber = intent.getStringExtra(Constraint.DATA_SEND)
    }

    fun addEvent() {
        btnSendPassword.setOnClickListener {
            if (GlobalHelper.networkIsConnected(this))
                sendNewPassword()
        }
    }

    override fun onConnectSocketSuccess() {
        //nothing to do
    }

    override fun onConnectSocketFail() {
        //nothing to do
    }

    private fun hideLoading() {
        layoutLoading.visibility = View.GONE
    }

    private fun showLoading() {
        layoutLoading.visibility = View.VISIBLE
    }

    private fun sendNewPassword() {
        if (etPhoneNumber.text.length <= 9) {
            showDialogMessage(null, resources.getString(R.string.register_phone_number_invalid))
            return
        }

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
                bundle.putString("PHONE_NUMBER", getPhoneNumber(etPhoneNumber?.text.toString()))
                bundle.putString("TOKEN_OTP", p0)

                val intent = Intent(this@ForgotPasswordActivity, OTPActivity::class.java)
                intent.putExtra(Constraint.DATA_SEND, bundle)
                startActivity(intent)
                finish()
            }
        }

        showLoading()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                getPhoneNumber(etPhoneNumber.text.toString()),        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                callback)     // OnVerificationStateChangedCallbacks
        hideKeyboard(etPhoneNumber)

    }
}
