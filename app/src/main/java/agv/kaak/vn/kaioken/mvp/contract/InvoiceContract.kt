package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.result.InvoiceInfo

interface InvoiceContract {
    interface View {
        fun getInvoiceDetailSuccess(invoiceInfo: InvoiceInfo)
        fun getInvoiceDetailFail(msg: String?)
    }

    interface Model {
        fun getInvoiceDetail(invoiceNo: String)
    }
}