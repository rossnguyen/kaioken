package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.DetailStoreService
import agv.kaak.vn.kaioken.entity.response.GetListOrderFormPaidResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.OrderFormPaidContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class OrderFormPaidModel(val view: OrderFormPaidContract.View) : OrderFormPaidContract.Model {
    private val TAG = OrderFormPaidModel::class.java.simpleName

    override fun getListOrderFormPaid(pageId: Int) {
        if (Constraint.mRetrofit == null) {
            view.getListOrderFomrPaidFail("")
            return
        }

        Constraint.mRetrofit?.create(DetailStoreService::class.java)
                ?.getOrderFormPaid(pageId,GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<GetListOrderFormPaidResponse>() {
                    override fun onComplete() {}

                    override fun onNext(response: GetListOrderFormPaidResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getListOrderFormPaidSuccess(response.listOrderFormPaid)
                        else {
                            view.getListOrderFomrPaidFail(
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
                        view.getListOrderFomrPaidFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}