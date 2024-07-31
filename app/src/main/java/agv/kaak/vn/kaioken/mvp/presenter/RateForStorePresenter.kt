package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.RateForStoreContract
import agv.kaak.vn.kaioken.mvp.model.RateForStoreModel

class RateForStorePresenter(val view:RateForStoreContract.View) {
    lateinit var model:RateForStoreModel

    init {
        this.model= RateForStoreModel(view)
    }

    fun rateForStore(pageId: Int, rate: Int, feedback: String, orderId:Int){
        this.model.rateForStore(pageId, rate, feedback, orderId)
    }
}