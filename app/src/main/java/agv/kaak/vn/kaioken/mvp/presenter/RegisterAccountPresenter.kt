package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.RegisterAccountContract
import agv.kaak.vn.kaioken.mvp.model.RegisterAccountModel

class RegisterAccountPresenter(val view:RegisterAccountContract.View) {
    val model:RegisterAccountModel= RegisterAccountModel(view)

    fun registerAccount(phone: String, token: String, deviceType: Int, deviceName: String,
                        userAgent: String, name: String, password: String, password2: String){
        model.registerAccount(phone, token, deviceType, deviceName, userAgent, name, password, password2)
    }

    fun checkPhoneExist(phone: String) {
        model.checkPhoneExist(phone)
    }
}