package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.UserService
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.response.LoginResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.ForgotPasswordContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class ForgotPasswordModel(val view: ForgotPasswordContract.View) : ForgotPasswordContract.Model {
    private val TAG = ForgotPasswordModel::class.java.simpleName
    override fun forgotPassword(phone: String, code: String, token: String) {
        if (Constraint.mRetrofit == null) {
            view.forgotPasswordFailed("")
            return
        }

        Constraint.mRetrofit?.create(UserService::class.java)
                //typeId = 0: customer
                //typeId = 1: staff
                ?.forgotPassword(phone, code, token, 0, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<LoginResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: LoginResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.forgotPasswordSuccess(response.loginResult!!)
                        else {
                            view.forgotPasswordFailed(
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
                        view.forgotPasswordFailed("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }

}