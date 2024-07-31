package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.response.RegisterAccountResponse
import agv.kaak.vn.kaioken.entity.result.UserInfo


interface RegisterAccountContract {
    interface View {
        fun registerAccountSuccess(registerAccountResponse: RegisterAccountResponse)
        fun registerAccountFailed(message: String)

        fun phoneNotExist()
        fun phoneExist(userInfo: UserInfo)
        fun checkPhoneExistFailed(msg: String?)
    }

    interface Model {
        fun registerAccount(phone: String, token: String, deviceType: Int, deviceName: String,
                            userAgent: String, name: String, password: String, password2: String)

        fun checkPhoneExist(phone: String)
    }
}