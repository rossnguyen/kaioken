package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.result.DetailCoupon

interface DetailCouponContract{
    interface View{
        fun getDetailCouponSuccess(detailCoupon:DetailCoupon)
        fun getDetailCouponFail(msg:String?)
    }

    interface Model{
        fun getDetailCoupon(pageId: Int, code: String)
    }
}