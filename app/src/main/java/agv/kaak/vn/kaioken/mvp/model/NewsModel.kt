package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.NewsService
import agv.kaak.vn.kaioken.entity.response.GetDetailNewsResponse
import agv.kaak.vn.kaioken.entity.response.GetListNewsResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.NewsContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class NewsModel(val view: NewsContract.View) : NewsContract.Model {
    private val TAG = NotificationModel::class.java.simpleName
    override fun getListNews() {
        if (Constraint.mRetrofit == null) {
            view.getListNewFail("")
            return
        }

        Constraint.mRetrofit?.create(NewsService::class.java)
                ?.getListNews(GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<GetListNewsResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: GetListNewsResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getListNewsSuccess(response.result!!)
                        else {
                            view.getListNewFail(
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
                        view.getListNewFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }

    override fun getDetailNews(newsId: Int) {
        if (Constraint.mRetrofit == null) {
            view.getDetailNewsFail("")
            return
        }

        Constraint.mRetrofit?.create(NewsService::class.java)
                ?.getDetailNews(newsId, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<GetDetailNewsResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: GetDetailNewsResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getDetailNewsSuccess(response.detailNews!!)
                        else {
                            view.getDetailNewsFail(
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
                        view.getDetailNewsFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}