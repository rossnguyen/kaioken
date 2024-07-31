package agv.kaak.vn.kaioken.mvp.presenter

import agv.kaak.vn.kaioken.entity.DetailWallet
import agv.kaak.vn.kaioken.mvp.contract.FinanceChartContract
import agv.kaak.vn.kaioken.mvp.model.FinanceChartModel


/**
 * Created by shakutara on 23/01/2018.
 */
class FinanceChartPresenter(private val mView: FinanceChartContract.View) {
    private val mModel = FinanceChartModel(mView)

    fun getFinanceAnalytic(id: Int?, typeId: Int?, year: Int?) {
        mModel.getFinanceAnalytic(id, typeId, year)
    }
}