package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.CheckTokenContract
import agv.kaak.vn.kaioken.mvp.model.CheckTokenModel

class CheckTokenPresenter(val view:CheckTokenContract.View) {
    val model=CheckTokenModel(view)

    fun checkToken(uid:String, sid:String){
        model.checkToken(uid, sid)
    }
}