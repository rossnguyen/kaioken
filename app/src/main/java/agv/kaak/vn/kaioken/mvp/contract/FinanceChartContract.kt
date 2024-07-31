package agv.kaak.vn.kaioken.mvp.contract

import agv.kaak.vn.kaioken.entity.DetailWallet


/**
 * Created by shakutara on 23/01/2018.
 */
interface FinanceChartContract {

    interface View {
        fun getFinanceAnalyticSuccess(detailWalletList: ArrayList<DetailWallet>?)
        fun getFinanceAnalyticFailed(msg: String?)
    }

    interface Model {
        fun getFinanceAnalytic(id: Int?, typeId: Int?, year: Int?)
    }

}