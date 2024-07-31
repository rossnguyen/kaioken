package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.UserService
import agv.kaak.vn.kaioken.entity.response.CheckTokenValidResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.CheckTokenContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class CheckTokenModel(val view: CheckTokenContract.View) : CheckTokenContract.Model {
    private val TAG = CheckTokenModel::class.java.simpleName
    override fun checkToken(uid: String, sid: String) {
        if (Constraint.mRetrofit == null) {
            view.checkTokenError("")
            return
        }

        Constraint.mRetrofit?.create(UserService::class.java)
                ?.checkTokenValid(uid, sid, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<CheckTokenValidResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: CheckTokenValidResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.checkTokenValid()
                        else {
                            view.checkTokenInValid(
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
                        view.checkTokenError("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}