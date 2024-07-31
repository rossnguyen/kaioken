package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.result.Coupon

interface CouponContract {
    interface View{
        fun getListCouponOnPageSuccess(listCoupon:ArrayList<Coupon>)
        fun getListCouponOnPageFail(message:String?)
    }

    interface Model{
        fun getListCouponOnPage(pageId:Int, customerId: Int)
    }

}