package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.CategoryStoreService
import agv.kaak.vn.kaioken.entity.response.CategoryStoreResponse
import agv.kaak.vn.kaioken.entity.response.CreateStoreCommentResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.CategoryStoreContract
import agv.kaak.vn.kaioken.utils.Constraint
import android.util.Log
import android.view.ViewTreeObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class CategoryStoreModel(val view: CategoryStoreContract.View) : CategoryStoreContract.Model {
    private val TAG = CategoryStoreModel::class.java.simpleName

    override fun getListCategoryStore() {
        if (Constraint.mRetrofit == null) {
            view.getCategoryRestaurantFail("")
            return
        }
        Constraint.mRetrofit?.create(CategoryStoreService::class.java)
                ?.getCategoryRestaurantList(GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<CategoryStoreResponse>() {
                    override fun onComplete() {}

                    override fun onNext(response: CategoryStoreResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getCategoryRestaurantSuccess(response.data!!)
                        else {
                            view.getCategoryRestaurantFail(
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
                        view.getCategoryRestaurantFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}