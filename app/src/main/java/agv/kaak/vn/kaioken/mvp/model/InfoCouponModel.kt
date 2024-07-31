package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.CouponService
import agv.kaak.vn.kaioken.entity.response.InfoCouponResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.InfoCouponContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class InfoCouponModel(val view: InfoCouponContract.View) : InfoCouponContract.Model {
    private val TAG = GetListImageStoreModel::class.java.simpleName
    override fun getInfoCoupon(couponId: Int) {
        if (Constraint.mRetrofit == null) {
            view.getInfoCouponFail("")
            return
        }

        Constraint.mRetrofit?.create(CouponService::class.java)
                ?.getDetailCoupon(couponId, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<InfoCouponResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: InfoCouponResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getInfoCouponSuccess(response.infoCoupon!!)
                        else {
                            view.getInfoCouponFail(
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
                        view.getInfoCouponFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}