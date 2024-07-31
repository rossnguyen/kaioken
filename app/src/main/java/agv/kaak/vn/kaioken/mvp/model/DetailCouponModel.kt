package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.CouponService
import agv.kaak.vn.kaioken.entity.response.DetailCouponResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.DetailCouponContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class DetailCouponModel(val view: DetailCouponContract.View) : DetailCouponContract.Model {
    private val TAG = DetailCouponModel::class.java.simpleName
    override fun getDetailCoupon(pageId: Int, code: String) {
        if (Constraint.mRetrofit == null) {
            view.getDetailCouponFail("")
            return
        }
        Constraint.mRetrofit?.create(CouponService::class.java)
                ?.getDetailCoupon(pageId, code, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<DetailCouponResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: DetailCouponResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getDetailCouponSuccess(response.detailCoupon!!)
                        else {
                            view.getDetailCouponFail(
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
                        view.getDetailCouponFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}