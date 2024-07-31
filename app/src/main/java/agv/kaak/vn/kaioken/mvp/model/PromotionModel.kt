package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.PromotionService
import agv.kaak.vn.kaioken.entity.response.GetListPromotionResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.PromotionContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class PromotionModel(val view: PromotionContract.View) : PromotionContract.Model {
    private val TAG = NotificationModel::class.java.simpleName
    override fun getListPromotion() {
        if (Constraint.mRetrofit == null) {
            view.getListPromotionFail("")
            return
        }

        Constraint.mRetrofit?.create(PromotionService::class.java)
                ?.getListPromotion(GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<GetListPromotionResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: GetListPromotionResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getListPromotionSuccess(response.listPromotion!!)
                        else {
                            view.getListPromotionFail(
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
                        view.getListPromotionFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}