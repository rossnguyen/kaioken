package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.StoreOnMapService
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.response.StoreOnMapResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.StoreOnMapContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class StoreOnMapModel(val view: StoreOnMapContract.View) : StoreOnMapContract.Model {
    private val TAG = StoreOnMapModel::class.java.simpleName
    override fun getListStoreOnMap(lat: Double, lng: Double, keyword: String, category_id: Int, distance: Int, is_using_app: Int, is_shipping: Int, is_online: Int, rating: Int, offset: Int, limit: Int) {
        if(Constraint.mRetrofit==null){
            view.onGetListStoreOnMapFail("")
            return
        }

        Constraint.mRetrofit!!.create(StoreOnMapService::class.java)
                .getListStoreOnmap(lat, lng, keyword, category_id, distance, is_using_app, is_shipping, is_online, rating, offset, limit, GlobalHelper.getHeaders())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : Observer<StoreOnMapResponse> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onNext(response: StoreOnMapResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.onGetListStoreOnMapSuccess(response.listStore!!)
                        else
                            view.onGetListStoreOnMapFail(
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
                        view.onGetListStoreOnMapFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }

                    override fun onComplete() {

                    }
                })
    }

    override fun getListStoreFollow(uid: Int, lat: Double, lng: Double, offset: Int, limit: Int) {
        if (Constraint.mRetrofit == null) {
            view.getListStoreFollowFail("")
            return
        }

        Constraint.mRetrofit!!.create(StoreOnMapService::class.java)
                .getListStoreFollow(uid, lat, lng, offset, limit,GlobalHelper.getHeaders())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : Observer<StoreOnMapResponse> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(response: StoreOnMapResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getListStoreFollowSuccess(response.listStore!!)
                        else
                            view.getListStoreFollowFail(
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
                        view.getListStoreFollowFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }

                    override fun onComplete() {

                    }
                })
    }

    override fun getListStoreOffer(lat: Double, lng: Double, distance: Int, offset: Int, limit: Int) {
        if (Constraint.mRetrofit == null) {
            view.getListStoreOfferFail("")
            return
        }

        Constraint.mRetrofit!!.create(StoreOnMapService::class.java)
                .getListStoreOffer(lat, lng, distance, offset, limit,GlobalHelper.getHeaders())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : Observer<StoreOnMapResponse> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(response: StoreOnMapResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getListStoreOfferSucces(response.listStore!!)
                        else {
                            view.getListStoreOfferFail(
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
                        view.getListStoreOfferFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }

                    override fun onComplete() {

                    }
                })
    }

    override fun sendLocationAtFirst(lat: Double, lng: Double) {
        if (Constraint.mRetrofit == null) {
            view.sendLocationAtFirstFail("")
            return
        }
        Constraint.mRetrofit!!.create(StoreOnMapService::class.java)
                .sendLocationAtFirst(lat, lng,GlobalHelper.getHeaders())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : Observer<BaseResponse> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(response: BaseResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.sendLocationAtFirstSuccess()
                        else {
                            view.sendLocationAtFirstFail(
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
                        view.sendLocationAtFirstFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }

                    override fun onComplete() {

                    }
                })
    }
}