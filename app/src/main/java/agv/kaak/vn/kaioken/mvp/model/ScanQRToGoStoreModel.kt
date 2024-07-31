package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.DetailStoreService
import agv.kaak.vn.kaioken.entity.response.LoginAnonymousResponse
import agv.kaak.vn.kaioken.entity.response.ScanQRToGoStoreResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.ScanQRToGoStoreContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class ScanQRToGoStoreModel(val view: ScanQRToGoStoreContract.View) : ScanQRToGoStoreContract.Model {
    private val TAG = ScanQRToGoStoreModel::class.java.simpleName
    override fun getStoreFromQR(sig: String, lat: Double, lng: Double) {
        if (Constraint.mRetrofit == null) {
            view.getStoreFromQRFail("")
            return
        }
        Constraint.mRetrofit?.create(DetailStoreService::class.java)
                ?.getInfoRestaurantFromQR(sig, lat, lng, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<ScanQRToGoStoreResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: ScanQRToGoStoreResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getStoreFromQRSuccess(response.resultScan!!)
                        else {
                            view.getStoreFromQRFail(
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
                        view.getStoreFromQRFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}