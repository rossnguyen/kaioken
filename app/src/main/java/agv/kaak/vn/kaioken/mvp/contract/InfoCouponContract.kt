package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.result.InfoCoupon

interface InfoCouponContract {
    interface View{
        fun getInfoCouponSuccess(infoCoupon:InfoCoupon)
        fun getInfoCouponFail(msg:String?)
    }

    interface Model{
        fun getInfoCoupon(couponId:Int)
    }
}