package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.response.RegisterAccountResponse
import agv.kaak.vn.kaioken.entity.result.UserInfo
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.RegisterAccountContract
import agv.kaak.vn.kaioken.mvp.presenter.RegisterAccountPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_enter_phone_number.*

class EnterPhoneNumberActivity : BaseActivity(), RegisterAccountContract.View {
    private var doubleBackToExitPressedOnce = false
    val presenter = RegisterAccountPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_phone_number)
        addEvent()
    }

    private fun addEvent() {
        btnStart.setOnClickListener {
            val phoneNumber = getPhoneNumber(etPhoneNumber.text.toString())
            if (phoneIsValid(phoneNumber)) {
                showLoading()
                presenter.checkPhoneExist(phoneNumber)
            }
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            forceCloseApp()
            return
        }
        doubleBackToExitPressedOnce = true
        showMessage(getString(R.string.press_once_to_exit))

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun registerAccountSuccess(registerAccountResponse: RegisterAccountResponse) {
        //nothing to do
    }

    override fun registerAccountFailed(message: String) {
        //nothing to do
    }

    override fun phoneNotExist() {
        hideLoading()
        goToRegisterActivity(etPhoneNumber.text.toString())
    }

    override fun phoneExist(userInfo: UserInfo) {
        hideLoading()
        goToLoginActivity(userInfo)
    }

    override fun checkPhoneExistFailed(msg: String?) {
        hideLoading()
        if (msg == null || msg.isEmpty())
            GlobalHelper.showMessage(applicationContext, resources.getString(R.string.all_error_global), true)
        else
            showDialogMessage(null, msg)
    }

    override fun onConnectSocketSuccess() {
        //nothing to do
    }

    override fun onConnectSocketFail() {
        //nothing to do
    }

    private fun showLoading() {
        layoutLoading?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        layoutLoading?.visibility = View.GONE
    }

    private fun goToRegisterActivity(phoneNumber: String) {
        val intentRegister = Intent(this, RegisterActivity::class.java)
        intentRegister.putExtra(Constraint.DATA_SEND, phoneNumber)
        startActivity(intentRegister)
    }

    private fun goToLoginActivity(userInfo: UserInfo) {
        val intentLogin = Intent(this, Login2Activity::class.java)
        intentLogin.putExtra(Constraint.DATA_SEND, userInfo)
        startActivity(intentLogin)
    }

    private fun phoneIsValid(phone: String?): Boolean {
        if (phone.isNullOrEmpty())
            return false
        else if (phone!!.length < 9) {
            showDialogMessage(null, resources.getString(R.string.register_phone_number_invalid))
            return false
        }
        return true
    }

    private fun getPhoneNumber(inputPhoneNumber: String): String {
        var defaultPhoneNumber = inputPhoneNumber
        if (defaultPhoneNumber.isEmpty())
            return ""
        if (defaultPhoneNumber.toCharArray()[0] == '0')
            defaultPhoneNumber = defaultPhoneNumber.replaceFirst("0", "+84")
        if (defaultPhoneNumber.toCharArray()[0] != '0' && defaultPhoneNumber.toCharArray()[0] != '+')
            defaultPhoneNumber = "+84$defaultPhoneNumber"
        return defaultPhoneNumber
    }
}
