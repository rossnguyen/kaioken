package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.result.UserInfo
import retrofit2.http.Field

interface UserContract {
    interface View {
        fun getUserInfoSuccess(userInfo:UserInfo)
        fun getUserInfoFail(msg:String?)

        fun updateUserInfoSuccess()
        fun updateUserInfoFail(msg:String?)

        fun changePasswordSuccess()
        fun changePasswordFail(msg:String?)
    }

    interface Model {
        fun getUserInfo()
        fun updateUserInfo(uid: String,
                           sid: String,
                           name: String,
                           email:String,
                           gender: Int,
                           birthday: String,
                           phone: String)

        fun changePassword(oldPassword: String,
                           newPassword: String,
                           confirmNewPassword: String)
    }
}