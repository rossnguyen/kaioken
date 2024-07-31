package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.LoginService
import agv.kaak.vn.kaioken.api.UserService
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.response.CheckVersionResponse
import agv.kaak.vn.kaioken.entity.response.LoginAnonymousResponse
import agv.kaak.vn.kaioken.entity.response.LoginResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.LoginContract
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.res.Resources
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class LoginModel(val view: LoginContract.View) : LoginContract.Model {
    private val TAG = LoginModel::class.java.simpleName

    override fun loginAnonymous(deviceName: String, deviceType: Int, userAgent: String, typeId: Int) {
        if (Constraint.mRetrofit == null) {
            view.loginAnonymousFail("")
            return
        }

        Constraint.mRetrofit?.create(LoginService::class.java)
                ?.loginAnonymous(deviceName, deviceType, userAgent, typeId, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<LoginAnonymousResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: LoginAnonymousResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.loginAnonymousSuccess(response.result!!)
                        else {
                            view.loginAnonymousFail(
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
                        view.loginAnonymousFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }

    override fun checkVersionLogin(version: String, deviceType: Int) {
        if (Constraint.mRetrofit == null) {
            view.checkVersionFail("")
            return
        }

        Constraint.mRetrofit?.create(LoginService::class.java)
                ?.checkVersionLogin(version, deviceType, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<CheckVersionResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: CheckVersionResponse) {
                        if (response.status == Constraint.STATUS_RIGHT) {
                            if (response.checkVersionResult == null)
                                view.versionValid()
                            else
                                view.versionInvalid(response.checkVersionResult!!)
                        } else
                            view.checkVersionFail(
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

                    override fun onError(e: Throwable) {
                        view.checkVersionFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }

    override fun login(phone: String, password: String, typeID: Int, deviceName: String, deviceType: Int, userAgent: String) {
        if (Constraint.mRetrofit == null) {
            view.loginFail("")
            return
        }

        Constraint.mRetrofit?.create(LoginService::class.java)
                ?.login(phone, password, typeID, deviceName, deviceType, userAgent, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<LoginResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: LoginResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.loginSuccess(response.loginResult!!)
                        else
                            view.loginFail(
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

                    override fun onError(e: Throwable) {
                        view.loginFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }

    override fun updateCloudMessagingToken(token: String) {
        if (Constraint.mRetrofit == null) {
            view.updateCloudMessagingTokenFailed("")
            return
        }

        Constraint.mRetrofit?.create(LoginService::class.java)
                ?.updateCloudToken(token, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<LoginResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: LoginResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.updateCloudMessagingTokenSuccess()
                        else
                            view.updateCloudMessagingTokenFailed(
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

                    override fun onError(e: Throwable) {
                        view.updateCloudMessagingTokenFailed("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}