package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.CommentStoreService
import agv.kaak.vn.kaioken.entity.response.CreateStoreCommentResponse
import agv.kaak.vn.kaioken.entity.response.GetStoreCommentResonse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.CommentStoreContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList

class CommentStoreModel(val view: CommentStoreContract.View) : CommentStoreContract.Model {
    private val TAG = CommentStoreModel::class.java.simpleName
    override fun createComment(uId: String, sId: String, pageId: Int, userId: Int, content: String, listLinkImage: ArrayList<String>) {
        if (Constraint.mRetrofit == null) {
            view.onCreateCommentFail("")
            return
        }
        Constraint.mRetrofit?.create(CommentStoreService::class.java)
                ?.createCommentRestaurant(uId, sId, pageId, userId, content, listLinkImage, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<CreateStoreCommentResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: CreateStoreCommentResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.onCreateCommentSuccess(response.result!!)
                        else {
                            view.onCreateCommentFail(
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
                        view.onCreateCommentFail(e.message!!)
                    }
                })
    }

    override fun getComment(pageId: Int) {
        if (Constraint.mRetrofit == null) {
            view.onGetCommentFail("")
            return
        }
        Constraint.mRetrofit?.create(CommentStoreService::class.java)
                ?.getCommentRestaurant(pageId, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<GetStoreCommentResonse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: GetStoreCommentResonse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.onGetCommentSuccess(response.listComment!!)
                        else {
                            view.onGetCommentFail(
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
                        view.onGetCommentFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}