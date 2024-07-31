package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.BillDetailInfo


/**
 * Created by shakutara on 15/03/2018.
 */
interface FinanceDetailContract {

    interface View {
        fun getBillDetailSuccess(billDetailInfo: BillDetailInfo?)
        fun getBillDetailFailed(msg: String?)
    }

    interface Model {
        fun getBillDetail(invoiceNo: String?)
    }

}