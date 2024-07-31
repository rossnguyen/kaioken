package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.entity.BillDetailInfo
import agv.kaak.vn.kaioken.mvp.contract.FinanceDetailContract
import agv.kaak.vn.kaioken.mvp.model.FinanceDetailModel


/**
 * Created by shakutara on 11/13/17.
 */
class FinanceDetailPresenter(private var mView: FinanceDetailContract.View) {
    private val mModel = FinanceDetailModel(mView)

    fun getBillDetail(invoiceNo: String?) {
        mModel.getBillDetail(invoiceNo)
    }
}