package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.StorePromotionContract
import agv.kaak.vn.kaioken.mvp.model.StorePromotionModel

class StorePromotionPresenter(val view:StorePromotionContract.View) {
    val model= StorePromotionModel(view)

    fun getDetailPromotion(promotionId:Int){
        model.getDetailStorePromotion(promotionId)
    }

    fun getListPromotion(pageId:Int){
        model.getListStorePromotion(pageId)
    }
}