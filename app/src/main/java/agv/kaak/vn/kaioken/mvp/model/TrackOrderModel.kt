package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.TrackOrderService
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.response.TrackOrderResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.TrackOrderContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TrackOrderModel(val view: TrackOrderContract.View) : TrackOrderContract.Model {
    private val TAG = TrackOrderModel::class.java.simpleName
    override fun getListOrdering() {
        if(Constraint.mRetrofit==null){
            view.getListOrderingFail("")
            return
        }

        Constraint.mRetrofit!!.create(TrackOrderService::class.java)
                .getListOrdering(GlobalHelper.getHeaders())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : Observer<TrackOrderResponse> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(response: TrackOrderResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getListOrderingSuccess(response.listOrdering!!)
                        else {
                            view.getListOrderingFail(
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
                        view.getListOrderingFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }

                    override fun onComplete() {

                    }
                })

    }

    override fun getListOrdered(offset: Int, limit: Int) {
        if(Constraint.mRetrofit==null){
            view.getListOrderedFail("")
            return
        }

        Constraint.mRetrofit!!.create(TrackOrderService::class.java)
                .getListOrdered(offset, limit, GlobalHelper.getHeaders())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : Observer<TrackOrderResponse> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(response: TrackOrderResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getListOrderedSuccess(response.listOrdering!!)
                        else {
                            view.getListOrderedFail(
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
                        view.getListOrderedFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }

                    override fun onComplete() {

                    }
                })

    }

    override fun removeOrder(orderId: Int) {
        if (Constraint.mRetrofit == null) {
            view.removeOrderFail("")
            return
        }

        Constraint.mRetrofit!!.create(TrackOrderService::class.java)
                .removeOrdered(orderId, GlobalHelper.getHeaders())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : Observer<BaseResponse> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(response: BaseResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.removeOrderSuccess()
                        else {
                            view.removeOrderFail(
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
                        view.removeOrderFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }

                    override fun onComplete() {

                    }
                })
    }

    override fun removeListOrder(listOrderId: ArrayList<Int>) {
        if (Constraint.mRetrofit == null) {
            view.removeListOrderFail("")
            return
        }

        Constraint.mRetrofit!!.create(TrackOrderService::class.java)
                .removeListOrdered(listOrderId, GlobalHelper.getHeaders())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : Observer<BaseResponse> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(response: BaseResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.removeListOrderSuccess()
                        else {
                            view.removeListOrderFail(
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
                        view.removeListOrderFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }

                    override fun onComplete() {

                    }
                })
    }
}