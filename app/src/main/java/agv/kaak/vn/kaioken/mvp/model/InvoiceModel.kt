package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.InvoiceService
import agv.kaak.vn.kaioken.entity.response.InvoiceInfoResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.InvoiceContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class InvoiceModel(val view: InvoiceContract.View) : InvoiceContract.Model {
    private val TAG = GetListImageStoreModel::class.java.simpleName
    override fun getInvoiceDetail(invoiceNo: String) {
        if (Constraint.mRetrofit == null) {
            view.getInvoiceDetailFail("")
            return
        }

        Constraint.mRetrofit?.create(InvoiceService::class.java)
                ?.getBillInfo(invoiceNo, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<InvoiceInfoResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: InvoiceInfoResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getInvoiceDetailSuccess(response.invoiceInfo!!)
                        else {
                            view.getInvoiceDetailFail(
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
                        view.getInvoiceDetailFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}