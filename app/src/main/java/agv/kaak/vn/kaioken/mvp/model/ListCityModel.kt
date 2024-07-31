package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.CityService
import agv.kaak.vn.kaioken.entity.response.GetListCityResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.ListCityContract
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.res.Resources
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class ListCityModel(val view: ListCityContract.View) : ListCityContract.Model {
    private val TAG = ListCityModel::class.java.simpleName
    override fun getListCity() {
        if (Constraint.mRetrofit == null) {
            view.getListCityFail("")
            return
        }

        Constraint.mRetrofit?.create(CityService::class.java)
                ?.getCityList(GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<GetListCityResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: GetListCityResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getListCitySuccess(response.listCity!!)
                        else {
                            view.getListCityFail(
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
                        view.getListCityFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}