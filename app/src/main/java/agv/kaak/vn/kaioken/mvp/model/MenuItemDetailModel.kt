package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.DetailStoreService
import agv.kaak.vn.kaioken.entity.ImageDetailItemFood
import agv.kaak.vn.kaioken.entity.response.MenuItemDetail
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.MenuItemDetailContract
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class MenuItemDetailModel(val view: MenuItemDetailContract.View) : MenuItemDetailContract.Model {
    private val TAG = MenuItemDetailModel::class.java.simpleName

    override fun getMenuItemDetail(menuId: Int) {
        if (Constraint.mRetrofit == null) {
            view.getMenuItemDetailFail("")
            return
        }

        Constraint.mRetrofit?.create(DetailStoreService::class.java)
                ?.getImageDetailFood(menuId, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<MenuItemDetail>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: MenuItemDetail) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            view.getMenuItemDetailSuccess(response.result!!)
                        else {
                            view.getMenuItemDetailFail(
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
                        view.getMenuItemDetailFail("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}