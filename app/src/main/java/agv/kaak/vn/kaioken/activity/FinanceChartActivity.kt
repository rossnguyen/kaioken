package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.DetailWallet
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.FinanceChartContract
import agv.kaak.vn.kaioken.mvp.presenter.FinanceChartPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.activity_finance_chart.*

class FinanceChartActivity : AppCompatActivity(), FinanceChartContract.View, View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imgBack -> onBackPressed()
        }
    }

    override fun getFinanceAnalyticSuccess(detailWalletList: ArrayList<DetailWallet>?) {
        hideLoading()
        showChart(detailWalletList)
    }

    private fun showChart(detailWalletList: ArrayList<DetailWallet>?) {
        val labels = ArrayList<String>()
        labels.add(getString(R.string.january))
        labels.add(getString(R.string.february))
        labels.add(getString(R.string.march))
        labels.add(getString(R.string.april))
        labels.add(getString(R.string.may))
        labels.add(getString(R.string.june))
        labels.add(getString(R.string.july))
        labels.add(getString(R.string.august))
        labels.add(getString(R.string.september))
        labels.add(getString(R.string.october))
        labels.add(getString(R.string.november))
        labels.add(getString(R.string.december))

        // for create Grouped Bar chart
        val groupIncoming = ArrayList<BarEntry>()
        val groupSpent = ArrayList<BarEntry>()

        detailWalletList?.indices?.forEach {
            groupIncoming.add(BarEntry(detailWalletList[it].totalThu?.toFloat()!!, it))
            groupSpent.add(BarEntry(detailWalletList[it].totalChi?.toFloat()!!, it))
        }

        val barDataSet1 = BarDataSet(groupIncoming, getString(R.string.finance_get_money))
        barDataSet1.setColors(COLUMN_SPEND_MONEY_COLORS)

        val barDataSet2 = BarDataSet(groupSpent, getString(R.string.finance_spent_money))
        barDataSet2.setColors(COLUMN_GET_MONEY_COLORS)

        val dataSets = ArrayList<BarDataSet>()
        dataSets.add(barDataSet1)
        dataSets.add(barDataSet2)

        val data = BarData(labels, dataSets)
        chartFinance.setDescription("")
        chartFinance.data = data
        chartFinance.animateY(TIME_ANIMATION)
    }

    override fun getFinanceAnalyticFailed(msg: String?) {
        hideLoading()
        GlobalHelper.showMessage(applicationContext,msg!!,true)
    }

    fun showLoading() {
        loader.visibility = View.VISIBLE
    }

    fun hideLoading() {
        loader.visibility = View.GONE
    }

    private var COLUMN_SPEND_MONEY_COLORS = intArrayOf(Color.rgb(0, 0, 255))
    private var COLUMN_GET_MONEY_COLORS = intArrayOf(Color.rgb(255, 0, 0))
    private var TIME_ANIMATION = 3000

    private val mPresenter = FinanceChartPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finance_chart)

        val bundle = intent.getBundleExtra(Constraint.TAG_BUNDLE)
        val id = bundle.getInt("OWNER_ID")
        val typeId = bundle.getInt("OWNER_TYPE_ID")
        val year = bundle.getInt("YEAR")

        showLoading()
        mPresenter.getFinanceAnalytic(id, typeId, year)

        //  views click
        imgBack?.setOnClickListener(this)

    }

}
