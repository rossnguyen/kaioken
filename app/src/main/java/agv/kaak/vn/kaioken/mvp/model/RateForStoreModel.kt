package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.RateForStoreService
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.RateForStoreContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RateForStoreModel(val view: RateForStoreContract.View) : RateForStoreContract.Model {
    private val TAG = RateForStoreModel::class.java.simpleName
    override fun rateForStore(pageId: Int, rate: Int, feedback: String, orderId: Int) {
        if (Constraint.mRetrofit == null) {
            view.onRateFail("")
            return
        }

        Constraint.mRetrofit!!.create(RateForStoreService::class.java)
                .RateForStore(pageId, rate, feedback, orderId,GlobalHelper.getHeaders())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : Observer<BaseResponse> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(baseResponse: BaseResponse) {
                        if (baseResponse.status == Constraint.STATUS_RIGHT)
                            view.onRateSuccess()
                        else {
                            view.onRateFail(
                                    if (baseResponse.error?.code == 0)
                                        baseResponse.error.desc!!
                                    else
                                        try {
                                            Constraint.errorMap!![baseResponse.error?.code]
                                        } catch (ex: Exception) {
                                            ""
                                        }
                            )
                            GlobalHelper.logE(TAG, baseResponse.error?.desc)
                        }

                    }

                    override fun onError(e: Throwable) {
                        view.onRateFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }

                    override fun onComplete() {

                    }
                })
    }
}