package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.OrderForm

interface OrderFormPaidContract {
    interface View {
        fun getListOrderFormPaidSuccess(orderFormPaid: ArrayList<OrderForm>)
        fun getListOrderFomrPaidFail(msg: String?)
    }

    interface Model {
        fun getListOrderFormPaid(pageId: Int)
    }
}