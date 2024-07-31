package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.UserService
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.response.LoginAnonymousResponse
import agv.kaak.vn.kaioken.entity.response.UpdateUserInfoResponse
import agv.kaak.vn.kaioken.entity.response.UserResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.UserContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class UserModel(val view: UserContract.View) : UserContract.Model {
    private val TAG = UserModel::class.java.simpleName
    override fun getUserInfo() {
        if (Constraint.mRetrofit == null) {
            view.getUserInfoFail("")
            return
        }

        Constraint.mRetrofit?.create(UserService::class.java)
                ?.getUserInfo(GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<UserResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: UserResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getUserInfoSuccess(response.user!!)
                        else {
                            view.getUserInfoFail(
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
                        view.getUserInfoFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }

    override fun updateUserInfo(uid: String, sid: String, name: String, email: String, gender: Int, birthday: String, phone: String) {
        if (Constraint.mRetrofit == null) {
            view.updateUserInfoFail("")
            return
        }

        Constraint.mRetrofit?.create(UserService::class.java)
                ?.updateUserInfo(uid, sid, name, email, gender, birthday, phone, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<UpdateUserInfoResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: UpdateUserInfoResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.updateUserInfoSuccess()
                        else {
                            view.updateUserInfoFail(
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
                        view.updateUserInfoFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }

    override fun changePassword(oldPassword: String, newPassword: String, confirmNewPassword: String) {
        if (Constraint.mRetrofit == null) {
            view.changePasswordFail("")
            return
        }

        Constraint.mRetrofit?.create(UserService::class.java)
                ?.changePassword(oldPassword, newPassword, confirmNewPassword, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<BaseResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: BaseResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.changePasswordSuccess()
                        else {
                            view.changePasswordFail(
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
                        view.changePasswordFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}