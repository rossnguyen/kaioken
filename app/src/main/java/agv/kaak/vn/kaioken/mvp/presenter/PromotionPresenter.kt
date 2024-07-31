package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.PromotionContract
import agv.kaak.vn.kaioken.mvp.model.PromotionModel

class PromotionPresenter(val view: PromotionContract.View) {
    val model = PromotionModel(view)

    fun getListPromotion() {
        model.getListPromotion()
    }
}