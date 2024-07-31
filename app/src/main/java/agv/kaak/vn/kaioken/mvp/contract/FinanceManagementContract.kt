package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.DetailWallet
import agv.kaak.vn.kaioken.entity.MoneyLimit
import agv.kaak.vn.kaioken.entity.response.wallet.ItemWallet


/**
 * Created by shakutara on 27/12/2017.
 */
interface FinanceManagementContract {

    interface View {
        fun getWalletSuccess(wallet: DetailWallet?)
        fun getWalletFailed(msg: String?)

        fun getWalletListViaTypeSuccess(itemWalletList: List<ItemWallet>?)
        fun getWalletListViaTypeFailed(msg: String?)

        fun saveWalletLimitSuccess(moneyLimit: MoneyLimit?)
        fun saveWalletLimitFailed(msg: String?)

        fun saveWalletSuccess(itemWallet: ItemWallet?)
        fun saveWalletFailed(msg: String?)

        fun deleteMoneySuccess(itemWallet: ItemWallet?)
        fun deleteMoneyFailed(msg: String?)
    }

    interface Model {
        fun getWallet(dateText: String, typeId: Int, storeId: Int?, offset: Int?, limit: Int?)
        fun getWalletListViaType(dateText: String, typeId: Int, storeId: Int?, walletType: Int?, offset: Int?, limit: Int?)
        fun saveWalletLimit(money: String, pageId: Int?, typeId: Int?)
        fun saveWallet(walletType: String?, title: String?, money: String?, dateText: String?, typeId: Int, storeId: Int?)
        fun deleteMoney(itemWalletId: String?)
    }

}