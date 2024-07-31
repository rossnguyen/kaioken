package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.LogoutContract
import agv.kaak.vn.kaioken.mvp.model.LogoutModel

class LogoutPresenter(val view:LogoutContract.View) {
    val model=LogoutModel(view)

    fun logout(){
        model.logout()
    }
}