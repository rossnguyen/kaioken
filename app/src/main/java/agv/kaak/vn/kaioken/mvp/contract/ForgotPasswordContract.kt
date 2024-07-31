package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.result.LoginResult

interface ForgotPasswordContract {
    interface View {
        fun forgotPasswordSuccess(loginResult:LoginResult)
        fun forgotPasswordFailed(msg: String?)
    }

    interface Model {
        fun forgotPassword(phone: String, code: String, token: String)
    }
}