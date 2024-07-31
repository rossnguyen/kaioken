package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.KiService
import agv.kaak.vn.kaioken.entity.response.GetKiOfPageResponse
import agv.kaak.vn.kaioken.entity.response.GetListKiResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.KiContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class KiModel(val view: KiContract.View) : KiContract.Model {
    private val TAG = KiModel::class.java.simpleName
    override fun getListKiOfUser(offset: Int, limit: Int) {
        if (Constraint.mRetrofit == null) {
            view.getListKiOfUserFail("")
            return
        }

        Constraint.mRetrofit?.create(KiService::class.java)
                ?.getListKi(offset, limit, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<GetListKiResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: GetListKiResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getListKiOfUserSuccess(response.kiOfUser!!)
                        else {
                            view.getListKiOfUserFail(
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
                        view.getListKiOfUserFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }

    override fun getKiOfPage(pageId: Int) {
        if (Constraint.mRetrofit == null) {
            view.getKiOfPageFail("")
            return
        }

        Constraint.mRetrofit?.create(KiService::class.java)
                ?.getKiOfPage(pageId, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<GetKiOfPageResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: GetKiOfPageResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getKiOfPageSuccess(response.kiOfPage!!)
                        else {
                            view.getKiOfPageFail(
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
                        view.getKiOfPageFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}