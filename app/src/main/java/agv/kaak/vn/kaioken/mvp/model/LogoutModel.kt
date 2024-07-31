package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.LoginService
import agv.kaak.vn.kaioken.api.UserService
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.LogoutContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class LogoutModel(val view: LogoutContract.View) : LogoutContract.Model {
    private val TAG = LogoutModel::class.java.simpleName
    override fun logout() {
        if (Constraint.mRetrofit == null) {
            view.logoutFail("")
            return
        }

        Constraint.mRetrofit?.create(LoginService::class.java)
                ?.logout(GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<BaseResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: BaseResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.logoutSuccess()
                        else {
                            view.logoutFail(
                                    if (response.error?.code == 0)
                                        response.error.desc!!
                                    else
                                        try {
                                            Constraint.errorMap?.get(response.error?.code)
                                        } catch (ex: Exception) {
                                            ""
                                        }
                            )
                        }
                    }

                    override fun onError(e: Throwable) {
                        view.logoutFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}