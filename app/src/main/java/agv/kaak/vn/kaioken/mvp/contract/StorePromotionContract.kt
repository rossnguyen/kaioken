package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.StorePromotion

interface StorePromotionContract {

    interface View{
        fun getDetailStorePromotionSuccess(promotion:StorePromotion)
        fun getDetailStorePromotionFail(msg:String?)

        fun getListStorePromotionSuccess(promotions:ArrayList<StorePromotion>)
        fun getListStorePromotionFail(msg:String?)
    }

    interface Model{
        fun getDetailStorePromotion(promotionId:Int)
        fun getListStorePromotion(pageId:Int)
    }
}