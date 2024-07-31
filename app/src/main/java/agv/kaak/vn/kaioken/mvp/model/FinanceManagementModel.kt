package agv.kaak.vn.kaioken.mvp.model

import agv.kaak.vn.kaioken.api.WalletService
import agv.kaak.vn.kaioken.mvp.contract.FinanceManagementContract
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.res.Resources
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import agv.kaak.vn.kaioken.entity.response.wallet.CreateWalletResponse
import agv.kaak.vn.kaioken.entity.response.wallet.WalletLimitResponse
import agv.kaak.vn.kaioken.entity.response.wallet.WalletListViaTypeResponse
import agv.kaak.vn.kaioken.entity.response.wallet.WalletResponse
import agv.kaak.vn.kaioken.helper.GlobalHelper

/**
 * Created by shakutara on 27/12/2017.
 */
class FinanceManagementModel(private val mView: FinanceManagementContract.View) : FinanceManagementContract.Model {
    private val TAG = FinanceManagementModel::class.java.simpleName
    override fun getWalletListViaType(dateText: String, typeId: Int, storeId: Int?, walletType: Int?, offset: Int?, limit: Int?) {
        if (Constraint.mRetrofit == null) {
            mView.getWalletListViaTypeFailed("")
            return
        }

        Constraint.mRetrofit?.create(WalletService::class.java)
                ?.getWalletListViaType(dateText, typeId, storeId, walletType, offset, limit, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<WalletListViaTypeResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: WalletListViaTypeResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            mView.getWalletListViaTypeSuccess(response.itemWalletList)
                        else {
                            mView.getWalletListViaTypeFailed(
                                    if (response.error?.code == 0)
                                        response.error.desc!!
                                    else
                                        Constraint.errorMap?.get(response.error?.code)!!
                            )
                            GlobalHelper.logE(TAG, response.error?.desc)
                        }
                    }

                    override fun onError(e: Throwable) {
                        mView.getWalletListViaTypeFailed("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }

    override fun deleteMoney(itemWalletId: String?) {
        if (Constraint.mRetrofit == null) {
            mView.deleteMoneyFailed("")
            return
        }
        Constraint.mRetrofit?.create(WalletService::class.java)
                ?.deleteMoney(itemWalletId, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<CreateWalletResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: CreateWalletResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            mView.deleteMoneySuccess(response.itemWallet)
                        else {
                            mView.deleteMoneyFailed(
                                    if (response.error?.code == 0)
                                        response.error.desc!!
                                    else
                                        Constraint.errorMap?.get(response.error?.code)!!
                            )
                            GlobalHelper.logE(TAG, response.error?.desc)
                        }
                    }

                    override fun onError(e: Throwable) {
                        val msg = Resources.getSystem().getString(android.R.string.unknownName)
                        mView.deleteMoneyFailed("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }

    override fun saveWallet(walletType: String?, title: String?, money: String?, dateText: String?, typeId: Int, storeId: Int?) {
        if (Constraint.mRetrofit == null) {
            mView.saveWalletFailed("")
            return
        }

        Constraint.mRetrofit?.create(WalletService::class.java)
                ?.saveWallet(walletType, title, money, dateText, typeId, storeId, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<CreateWalletResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: CreateWalletResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            mView.saveWalletSuccess(response.itemWallet)
                        else {
                            mView.saveWalletFailed(
                                    if (response.error?.code == 0)
                                        response.error.desc!!
                                    else
                                        Constraint.errorMap?.get(response.error?.code)!!
                            )
                            GlobalHelper.logE(TAG, response.error?.desc)
                        }
                    }

                    override fun onError(e: Throwable) {
                        val msg = Resources.getSystem().getString(android.R.string.unknownName)
                        mView.saveWalletFailed("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }

    override fun saveWalletLimit(money: String, pageId: Int?, typeId: Int?) {
        if (Constraint.mRetrofit == null) {
            mView.saveWalletLimitFailed("")
            return
        }

        Constraint.mRetrofit?.create(WalletService::class.java)
                ?.saveWalletLimit(money, pageId, typeId, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<WalletLimitResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: WalletLimitResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            mView.saveWalletLimitSuccess(response.moneyLimit)
                        else {
                            mView.saveWalletLimitFailed(
                                    if (response.error?.code == 0)
                                        response.error.desc!!
                                    else
                                        Constraint.errorMap?.get(response.error?.code)!!
                            )
                            GlobalHelper.logE(TAG, response.error?.desc)
                        }
                    }

                    override fun onError(e: Throwable) {
                        val msg = Resources.getSystem().getString(android.R.string.unknownName)
                        mView.saveWalletLimitFailed("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }

    override fun getWallet(dateText: String, typeId: Int, storeId: Int?, offset: Int?, limit: Int?) {
        if (Constraint.mRetrofit == null) {
            mView.getWalletFailed("")
            return
        }
        Constraint.mRetrofit?.create(WalletService::class.java)
                ?.getWalletInfo(dateText, typeId, storeId, offset, limit, GlobalHelper.getHeaders())
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableObserver<WalletResponse>() {
                    override fun onComplete() {

                    }

                    override fun onNext(response: WalletResponse) {
                        if (response.status == Constraint.STATUS_RIGHT)
                            mView.getWalletSuccess(response.detail)
                        else {
                            mView.getWalletFailed(
                                    if (response.error?.code == 0)
                                        response.error.desc!!
                                    else
                                        Constraint.errorMap?.get(response.error?.code)!!
                            )
                            GlobalHelper.logE(TAG, response.error?.desc)
                        }
                    }

                    override fun onError(e: Throwable) {
                        val msg = Resources.getSystem().getString(android.R.string.unknownName)
                        mView.getWalletFailed("")
                        GlobalHelper.logE(TAG, e.message)
                    }
                })
    }
}