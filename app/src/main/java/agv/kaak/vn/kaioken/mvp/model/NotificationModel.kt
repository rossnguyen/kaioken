package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.NotificationService
import agv.kaak.vn.kaioken.entity.response.NotificationResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.NotificationContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class NotificationModel(val view: NotificationContract.View) : NotificationContract.Model {
    private val TAG = NotificationModel::class.java.simpleName

    override fun getListNoticeNoti(userId: Int, departmentId: Int, pageId: Int, offset: Int, limit: Int) {
        if (Constraint.mRetrofit == null) {
            view.getListNoticeFail("")
            return
        }

        Constraint.mRetrofit?.create(NotificationService::class.java)
                ?.getListNotifyNotice(userId, departmentId, pageId, offset, limit, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<NotificationResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: NotificationResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getListNoticeNotiSuccess(response.listNoti!!)
                        else {
                            view.getListNoticeFail(
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
                        view.getListNoticeFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}