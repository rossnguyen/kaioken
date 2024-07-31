package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.DetailStoreService
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.FollowStoreConstract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class FollowStoreModel(val view: FollowStoreConstract.View) : FollowStoreConstract.Model {
    private val TAG = FollowStoreModel::class.java.simpleName
    override fun followStore(uid: Int, pageId: Int) {
        if (Constraint.mRetrofit == null) {
            view.followStoreFail("")
            return
        }
        Constraint.mRetrofit?.create(DetailStoreService::class.java)
                ?.followRestaurant(uid, pageId, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<BaseResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: BaseResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.followStoreSuccess()
                        else {
                            view.followStoreFail(
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
                        view.followStoreFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}