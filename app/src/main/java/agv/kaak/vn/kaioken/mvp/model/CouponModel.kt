package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.CouponService
import agv.kaak.vn.kaioken.entity.response.GetListCouponUserOnPageResponse
import agv.kaak.vn.kaioken.entity.result.Coupon
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.CouponContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class CouponModel(val view: CouponContract.View) : CouponContract.Model {
    private val TAG = CouponModel::class.java.simpleName
    override fun getListCouponOnPage(pageId: Int, customerId: Int) {
        if (Constraint.mRetrofit == null) {
            view.getListCouponOnPageFail("")
            return
        }
        Constraint.mRetrofit?.create(CouponService::class.java)
                ?.getListCouponOfUserOnPage(pageId, customerId, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<GetListCouponUserOnPageResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: GetListCouponUserOnPageResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getListCouponOnPageSuccess(response.listCoupon)
                        else {
                            view.getListCouponOnPageFail(
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
                        view.getListCouponOnPageFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}