package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.OrderFormPaidContract
import agv.kaak.vn.kaioken.mvp.model.OrderFormPaidModel

class OrderFormPaidPresenter(val view: OrderFormPaidContract.View) {
    val model = OrderFormPaidModel(view)

    fun getListOrderFormPaid(pageId: Int) {
        model.getListOrderFormPaid(pageId)
    }
}