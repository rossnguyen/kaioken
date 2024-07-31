package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.DetailStoreService
import agv.kaak.vn.kaioken.entity.response.ImageDetailStoreResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.GetListImageStoreContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class GetListImageStoreModel(val view: GetListImageStoreContract.View) : GetListImageStoreContract.Model {
    private val TAG = GetListImageStoreModel::class.java.simpleName
    override fun getListImageStore(storeId: Int) {
        if (Constraint.mRetrofit == null) {
            view.getListImageStoreFail("")
            return
        }

        Constraint.mRetrofit?.create(DetailStoreService::class.java)
                ?.getImageDetail(storeId, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<ImageDetailStoreResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: ImageDetailStoreResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getListImageStoreSuccess(response.listImage!!)
                        else {
                            view.getListImageStoreFail(
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
                        view.getListImageStoreFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}