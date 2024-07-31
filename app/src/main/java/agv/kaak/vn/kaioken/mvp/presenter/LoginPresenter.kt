package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.LoginContract
import agv.kaak.vn.kaioken.mvp.model.LoginModel
import agv.kaak.vn.kaioken.mvp.model.UserModel

class LoginPresenter(val view:LoginContract.View) {
    val loginModel=LoginModel(view)


    fun loginAnonymous(deviceName:String, deviceType:Int, userAgent:String, typeId:Int){
        this.loginModel.loginAnonymous(deviceName, deviceType, userAgent, typeId)
    }

    fun checkVersionLogin(version:String, appTypeId: Int){
        this.loginModel.checkVersionLogin(version, appTypeId)
    }

    fun login(phone: String, password: String, typeID: Int, deviceName: String, deviceType: Int, userAgent: String){
        this.loginModel.login(phone, password, typeID, deviceName, deviceType, userAgent)
    }
    fun updateCloudMessagingToken(token: String){
        this.loginModel.updateCloudMessagingToken(token)
    }
}