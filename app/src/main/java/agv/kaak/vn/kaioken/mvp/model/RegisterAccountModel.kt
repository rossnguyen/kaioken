package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.RegisterAccountService
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.define.UseType
import agv.kaak.vn.kaioken.entity.response.CheckPhoneExistResponse
import agv.kaak.vn.kaioken.entity.response.RegisterAccountResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.RegisterAccountContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class RegisterAccountModel(val view: RegisterAccountContract.View) : RegisterAccountContract.Model {
    private val TAG = RegisterAccountModel::class.java.simpleName
    override fun registerAccount(phone: String, token: String, deviceType: Int, deviceName: String, userAgent: String, name: String, password: String, password2: String) {
        if (Constraint.mRetrofit == null) {
            view.registerAccountFailed("")
            return
        }
        Constraint.mRetrofit?.create(RegisterAccountService::class.java)
                ?.registerAccount(phone, token, deviceType, deviceName, userAgent, name, password, password2, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<RegisterAccountResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: RegisterAccountResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.registerAccountSuccess(response)
                        else {
                            view.registerAccountFailed(
                                    if (response.error?.code == 0)
                                        response.error.desc!!
                                    else
                                        try {
                                            Constraint.errorMap?.get(response.error?.code)!!
                                        } catch (ex: Exception) {
                                            ""
                                        }
                            )
                            GlobalHelper.logE(TAG, response.error?.desc)
                        }
                    }

                    override fun onError(e: Throwable) {
                        view.registerAccountFailed("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }

    override fun checkPhoneExist(phone: String) {
        if (Constraint.mRetrofit == null) {
            view.checkPhoneExistFailed("")
            return
        }
        Constraint.mRetrofit?.create(RegisterAccountService::class.java)
                ?.checkPhoneExist(phone, 0, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<CheckPhoneExistResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: CheckPhoneExistResponse) {
                        if (response.status == Constraint.STATUS_RIGHT) {
                            if (response.userInfo == null)
                                view.phoneNotExist()
                            else
                                view.phoneExist(response.userInfo!!)
                        } else {
                            view.checkPhoneExistFailed(
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
                        view.checkPhoneExistFailed("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}