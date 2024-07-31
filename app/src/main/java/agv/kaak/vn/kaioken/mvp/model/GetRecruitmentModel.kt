package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.DetailStoreService
import agv.kaak.vn.kaioken.entity.response.DetailRecruitmentResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.GetRecruitmentContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class GetRecruitmentModel(val view: GetRecruitmentContract.View) : GetRecruitmentContract.Model {
    private val TAG = GetRecruitmentModel::class.java.simpleName
    override fun getDetailRecruitment(storeId: Int) {
        if (Constraint.mRetrofit == null) {
            view.getDetailRecruitmentFail("")
            return
        }

        Constraint.mRetrofit?.create(DetailStoreService::class.java)
                ?.getDetailRecruitment(storeId, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<DetailRecruitmentResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: DetailRecruitmentResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getDetailRecruitmentSuccess(response.result!!)
                        else {
                            view.getDetailRecruitmentFail(
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
                        view.getDetailRecruitmentFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}