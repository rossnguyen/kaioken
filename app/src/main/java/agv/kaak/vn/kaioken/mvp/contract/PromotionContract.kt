package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.Promotion

interface PromotionContract {
    interface View {
        fun getListPromotionSuccess(listPromotion: ArrayList<Promotion>)
        fun getListPromotionFail(msg: String?)
    }

    interface Model {
        fun getListPromotion()
    }
}