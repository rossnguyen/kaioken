package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.DetailCouponContract
import agv.kaak.vn.kaioken.mvp.model.DetailCouponModel

class DetailCouponPresenter(val view:DetailCouponContract.View) {
    val model=DetailCouponModel(view)

    fun getDetailCoupon(pageId:Int, code:String){
        model.getDetailCoupon(pageId, code)
    }
}