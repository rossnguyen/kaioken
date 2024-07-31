package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.CouponService
import agv.kaak.vn.kaioken.api.DetailStoreService
import agv.kaak.vn.kaioken.entity.response.DetailPromotionResponse
import agv.kaak.vn.kaioken.entity.response.GetListStorePromotionResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.StorePromotionContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class StorePromotionModel(val view: StorePromotionContract.View) : StorePromotionContract.Model {
    private val TAG = StorePromotionModel::class.java.simpleName
    override fun getDetailStorePromotion(promotionId: Int) {
        if (Constraint.mRetrofit == null) {
            view.getDetailStorePromotionFail("")
            return
        }

        Constraint.mRetrofit?.create(CouponService::class.java)
                ?.getDetailPromotion(promotionId, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<DetailPromotionResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: DetailPromotionResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getDetailStorePromotionSuccess(response.detailPromotion!!)
                        else {
                            view.getDetailStorePromotionFail(
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
                        view.getDetailStorePromotionFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }

    override fun getListStorePromotion(pageId: Int) {
        if (Constraint.mRetrofit == null) {
            view.getListStorePromotionFail("")
            return
        }

        Constraint.mRetrofit?.create(DetailStoreService::class.java)
                ?.getListPromotion(pageId, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<GetListStorePromotionResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: GetListStorePromotionResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getListStorePromotionSuccess(response.promotions)
                        else {
                            view.getListStorePromotionFail(
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
                        view.getListStorePromotionFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}