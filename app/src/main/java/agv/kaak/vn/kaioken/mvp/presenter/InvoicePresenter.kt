package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.InvoiceContract
import agv.kaak.vn.kaioken.mvp.model.InvoiceModel

class InvoicePresenter(val view:InvoiceContract.View) {
    val model=InvoiceModel(view)

    fun getInvoiceDetail(invoiceNo:String){
        model.getInvoiceDetail(invoiceNo)
    }
}