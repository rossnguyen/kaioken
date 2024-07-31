package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.WalletService
import agv.kaak.vn.kaioken.entity.response.wallet.BillDetailInfoResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.FinanceDetailContract
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.res.Resources
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by shakutara on 11/13/17.
 */
class FinanceDetailModel(private var mView: FinanceDetailContract.View) : FinanceDetailContract.Model {
    private val TAG = FinanceDetailModel::class.java.simpleName
    override fun getBillDetail(invoiceNo: String?) {
        if (Constraint.mRetrofit == null) {
            mView.getBillDetailFailed("")
            return
        }

        Constraint.mRetrofit?.create(WalletService::class.java)
                ?.getBillDetailViaInvoiceNo(invoiceNo, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<BillDetailInfoResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: BillDetailInfoResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            mView.getBillDetailSuccess(response.billDetailInfo)
                        else {
                            mView.getBillDetailFailed(
                                    if (response.error?.code == 0)
                                        response.error.desc!!
                                    else
                                        try {
                                            Constraint.errorMap?.get(response.error?.code)
                                        } catch (ex: Exception) {
                                            ""
                                        }
                            )
                            GlobalHelper.logE(TAG, response.error?.desc)
                        }
                    }

                    override fun onError(e: Throwable) {
                        mView.getBillDetailFailed("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}