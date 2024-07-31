package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.DetailStoreService
import agv.kaak.vn.kaioken.entity.response.DetailStoreResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.DetailStoreContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class DetailStoreModel(val view: DetailStoreContract.View) : DetailStoreContract.Model {
    private val TAG = DetailStoreModel::class.java.simpleName
    override fun getDetailStore(pageId: Int, uid: Int) {
        if (Constraint.mRetrofit == null) {
            view.getDetailStoreFail("")
            return
        }
        Constraint.mRetrofit?.create(DetailStoreService::class.java)
                ?.getInfoRestaurant(pageId, uid, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<DetailStoreResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: DetailStoreResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getDetailStoreSuccess(response.detailStoreResult!!)
                        else {
                            view.getDetailStoreFail(
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
                        view.getDetailStoreFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }

}