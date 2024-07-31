package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.WalletService
import agv.kaak.vn.kaioken.entity.DetailWallet
import agv.kaak.vn.kaioken.entity.response.wallet.DetailWalletResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.FinanceChartContract
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.res.Resources
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by shakutara on 23/01/2018.
 */
class FinanceChartModel(private var mView: FinanceChartContract.View) : FinanceChartContract.Model {
    private val TAG = FinanceChartModel::class.java.simpleName
    override fun getFinanceAnalytic(id: Int?, typeId: Int?, year: Int?) {
        if (Constraint.mRetrofit == null) {
            mView.getFinanceAnalyticFailed("")
            return
        }
        Constraint.mRetrofit?.create(WalletService::class.java)
                ?.getFinanceAnalytic(id, typeId, year, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<DetailWalletResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: DetailWalletResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            mView.getFinanceAnalyticSuccess(response.detailWalletList as ArrayList<DetailWallet>?)
                        else {
                            mView.getFinanceAnalyticFailed(
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
                        mView.getFinanceAnalyticFailed("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}