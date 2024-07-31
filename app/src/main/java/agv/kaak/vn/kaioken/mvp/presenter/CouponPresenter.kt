package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.CouponContract
import agv.kaak.vn.kaioken.mvp.model.CouponModel

class CouponPresenter(view:CouponContract.View) {
    val model=CouponModel(view)

    fun getListCouponOnPage(pageId:Int, customerId:Int){
        model.getListCouponOnPage(pageId, customerId)
    }
}