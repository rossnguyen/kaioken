package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.CheckVersionResult
import agv.kaak.vn.kaioken.entity.result.LoginAnonymousResult
import agv.kaak.vn.kaioken.entity.result.LoginResult

interface LoginContract {
    interface View{
        fun loginAnonymousSuccess(data:LoginAnonymousResult)
        fun loginAnonymousFail(message:String?)

        fun versionValid()
        fun versionInvalid(newVersion:CheckVersionResult)
        fun checkVersionFail(msg:String?)

        fun loginSuccess(infoUser: LoginResult)
        fun loginFail(message: String?)

        fun updateCloudMessagingTokenSuccess()
        fun updateCloudMessagingTokenFailed(msg: String?)
    }

    interface Model{
        fun loginAnonymous(deviceName:String, deviceType:Int, userAgent:String, typeId:Int)
        fun checkVersionLogin(version:String, deviceType: Int)
        fun login(phone: String, password: String, typeID: Int, deviceName: String, deviceType: Int, userAgent: String)
        fun updateCloudMessagingToken(token: String)
    }
}