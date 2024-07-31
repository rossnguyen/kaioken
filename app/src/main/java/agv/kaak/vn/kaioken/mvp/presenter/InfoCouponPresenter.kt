package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.InfoCouponContract
import agv.kaak.vn.kaioken.mvp.model.InfoCouponModel

class InfoCouponPresenter(val view:InfoCouponContract.View) {
    val model=InfoCouponModel(view)

    fun getInfoCoupon(couponId:Int){
        model.getInfoCoupon(couponId)
    }
}