package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.mvp.contract.FinanceManagementContract
import agv.kaak.vn.kaioken.mvp.model.FinanceManagementModel


/**
 * Created by shakutara on 27/12/2017.
 */
class FinanceManagementPresenter(val mView: FinanceManagementContract.View){
    private val mModel = FinanceManagementModel(mView)

    fun getWalletListViaType(dateText: String, typeId: Int, storeId: Int?, walletType: Int?, offset: Int?, limit: Int?) {
        mModel.getWalletListViaType(dateText, typeId, storeId, walletType, offset, limit)
    }

    fun deleteMoney(itemWalletId: String?) {
        mModel.deleteMoney(itemWalletId)
    }

    fun saveWallet(walletType: String?, title: String?, money: String?, dateText: String?, typeId: Int, storeId: Int?) {
        mModel.saveWallet(walletType, title, money, dateText, typeId, storeId)
    }

    fun saveWalletLimit(money: String, pageId: Int?, typeId: Int?) {
        mModel.saveWalletLimit(money, pageId, typeId)
    }

    fun getWallet(dateText: String, typeId: Int, storeId: Int?, offset: Int?, limit: Int?) {
        mModel.getWallet(dateText, typeId, storeId, offset, limit)
    }
}