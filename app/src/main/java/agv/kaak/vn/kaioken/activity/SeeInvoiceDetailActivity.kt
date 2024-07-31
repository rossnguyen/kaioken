package agv.kaak.vn.kaioken.activity

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.FoodOnInvoiceAdapter
import agv.kaak.vn.kaioken.entity.DetailOrder
import agv.kaak.vn.kaioken.entity.result.InvoiceInfo
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.InvoiceContract
import agv.kaak.vn.kaioken.mvp.presenter.InvoicePresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.activity_see_invoice_detail.*
import kotlin.math.roundToInt

class SeeInvoiceDetailActivity : AppCompatActivity(), InvoiceContract.View {

    private var invoiceNo: String = ""
    private var invoicePresenter = InvoicePresenter(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_see_invoice_detail)
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        getData()
        addEvent()
        loadData()
    }

    private fun getData() {
        invoiceNo = intent.getStringExtra(Constraint.DATA_SEND)
    }

    private fun addEvent() {
        ibtnClose.setOnClickListener {
            finish()
        }
    }

    private fun loadData() {
        showLoading()
        invoicePresenter.getInvoiceDetail(invoiceNo)
    }

    private fun bindData(invoiceInfo: InvoiceInfo) {

        invoiceInfo.invoiceInfo?.apply {
            if (discountValue == null)
                discountValue = 0.toDouble()

            if (discountFinalMatch == null)
                discountFinalMatch = 0.toDouble()

            if (totalItemDiscountValue == null)
                totalItemDiscountValue = 0.toDouble()
        }

        tvName?.text = invoiceInfo.pageInfo?.pageName
        tvAddress?.text = invoiceInfo.pageInfo?.address
        tvPhoneNumber?.text = resources.getString(R.string.invoice_detail_phone, invoiceInfo.pageInfo?.phone)
        tvInvoiceNo?.text = resources.getString(R.string.format_invoice_no_x, invoiceInfo.invoiceInfo?.invoiceNo)
        tvDate?.text = ConvertHelper.reverseDate(invoiceInfo.invoiceInfo?.dateAdd!!.split(" ")[0], "-")

        if (invoiceInfo.invoiceInfo?.isStaffOrder == 0) {
            tvCustomerName?.text = resources.getString(R.string.invoice_detail_customer, invoiceInfo.customerInfo?.userName)
            tvCashierName?.text = resources.getString(R.string.invoice_detail_cashier,
                    if (invoiceInfo.cashierInfo?.name == null) "" else invoiceInfo.cashierInfo?.name)
        } else {
            tvCustomerName?.text = resources.getString(R.string.invoice_detail_customer, invoiceInfo.invoiceInfo?.customerName)
            tvCashierName?.text = resources.getString(R.string.invoice_detail_cashier, invoiceInfo.invoiceInfo?.staffName)
        }

        tvShift?.text = resources.getString(R.string.invoice_detail_shift,
                if (invoiceInfo.invoiceInfo?.shift == null) ""
                else invoiceInfo.invoiceInfo?.shift)

        tvTimeIn?.text = resources.getString(R.string.invoice_detail_time_in,
                invoiceInfo.invoiceInfo?.dateAdd!!.split(" ")[1])
        tvTimeOut?.text = resources.getString(R.string.invoice_detail_time_out,
                if (invoiceInfo.invoiceInfo?.dateComplete == null) "" else invoiceInfo.invoiceInfo?.dateComplete!!.split(" ")[1])
        tvTable?.text = resources.getString(R.string.invoice_detail_table,
                if (invoiceInfo.tableInfo?.tableName == null) resources.getString(R.string.all_unknown) else invoiceInfo.tableInfo?.tableName)
        tvSumQuantity?.text = "${invoiceInfo.getTotalItem()}"
        tvSumMoneyOrder?.text = ConvertHelper.doubleToMoney(applicationContext,(invoiceInfo.invoiceInfo?.priceBefore!! - invoiceInfo.invoiceInfo?.totalItemDiscountValue!!))
        invoiceInfo.invoiceInfo?.apply {
            tvSumMoney?.text = ConvertHelper.doubleToMoney(applicationContext,priceAfter!!)
        }

        if (invoiceInfo.invoiceInfo?.discountFinalMatch != null && invoiceInfo.invoiceInfo?.discountFinalMatch!! > 0) {
            layoutDiscount.visibility = View.VISIBLE
            tvDiscount.text = ConvertHelper.doubleToMoney(applicationContext, invoiceInfo.invoiceInfo?.discountFinalMatch!!)
        }
        if (invoiceInfo.invoiceInfo?.extraValue != null && invoiceInfo.invoiceInfo?.extraValue!! > 0) {
            layoutExtra.visibility = View.VISIBLE
            tvExtra.text = ConvertHelper.doubleToMoney(applicationContext, invoiceInfo.invoiceInfo?.extraValue!!)
        }
        if (invoiceInfo.invoiceInfo?.vatValue != null && invoiceInfo.invoiceInfo?.vatValue!! > 0) {
            layoutVAT.visibility = View.VISIBLE
            tvVAT.text =ConvertHelper.doubleToMoney(applicationContext,invoiceInfo.invoiceInfo?.vatValue!!)
        }
        tvReceivedMoney?.text = if (invoiceInfo.invoiceInfo?.moneyReceive == null) "0" else ConvertHelper.doubleToMoney(applicationContext,invoiceInfo.invoiceInfo?.moneyReceive!!)
        tvExcess?.text = if (invoiceInfo.invoiceInfo?.moneyExcess == null) "0" else ConvertHelper.doubleToMoney(applicationContext,invoiceInfo.invoiceInfo?.moneyExcess!!)

        initListOrder(invoiceInfo.listItem!!)
    }

    private fun initListOrder(orders: ArrayList<DetailOrder>) {
        val adapter = FoodOnInvoiceAdapter(applicationContext, orders)

        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        lstOrder.adapter = adapter
        lstOrder.layoutManager = layoutManager
    }

    override fun getInvoiceDetailSuccess(invoiceInfo: InvoiceInfo) {
        hideLoading()
        bindData(invoiceInfo)
    }

    override fun getInvoiceDetailFail(msg: String?) {
        hideLoading()
        if (msg == "")
            GlobalHelper.showMessage(applicationContext, resources.getString(R.string.all_error_global), true)
        else
            GlobalHelper.showMessage(applicationContext, msg!!, true)
    }

    private fun showLoading() {
        layoutLoading.visibility = View.VISIBLE
        layoutContent.visibility = View.GONE
    }

    private fun hideLoading() {
        layoutLoading.visibility = View.GONE
        layoutContent.visibility = View.VISIBLE
    }
}
