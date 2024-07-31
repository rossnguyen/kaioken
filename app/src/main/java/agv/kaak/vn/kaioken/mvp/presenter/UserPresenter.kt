package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.UserContract
import agv.kaak.vn.kaioken.mvp.model.UserModel

class UserPresenter(val view:UserContract.View):UserContract.Model {
    val model=UserModel(view)

    override fun getUserInfo() {
        model.getUserInfo()
    }

    override fun updateUserInfo(uid: String, sid: String, name: String, email:String, gender: Int, birthday: String, phone: String) {
        model.updateUserInfo(uid, sid, name,email, gender, birthday, phone)
    }

    override fun changePassword(oldPassword: String, newPassword: String, confirmNewPassword: String) {
        model.changePassword(oldPassword, newPassword, confirmNewPassword)
    }
}