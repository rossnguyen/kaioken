package agv.kaak.vn.kaioken.fragment.process


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.ShowListFoodOnTrackingAdapter
import agv.kaak.vn.kaioken.entity.DetailOrder
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.GlobalHelper
import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.fragment_detail_order.*


/**
 * A simple [Fragment] subclass.
 *
 */
class DetailOrderFragment : BaseFragment() {

    private var listOrderFood: ArrayList<DetailOrder> = arrayListOf()
    private var invoiceNo: String = ""
    private var sumMoney: Double = 0.toDouble()
    private var discount: Double = 0.toDouble()
    lateinit var adapter: ShowListFoodOnTrackingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_order, container, false)
    }

    override fun getData() {
        listOrderFood.addAll(arguments!!.getSerializable(ProcessFragment.LIST_ORDER_SEND) as ArrayList<DetailOrder>)
        invoiceNo = arguments!!.getString(ProcessFragment.INVOICE_NO_SEND)
        sumMoney = arguments!!.getDouble(ProcessFragment.SUM_MONEY_SEND)
        discount = arguments!!.getDouble(ProcessFragment.DISCOUNT_SEND)
    }

    override fun loadData() {
        ViewCompat.setElevation(layoutTop, resources.getDimension(R.dimen.all_masterial_elevation_searchbar_scroll))
        //init list order
        adapter = ShowListFoodOnTrackingAdapter(activityParent, listOrderFood)
        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        lstOrder.adapter = adapter
        lstOrder.layoutManager = layoutManager

        //set text for invoiceNo and priceBefore
        tvInvoiceNo.text = invoiceNo
        bindSummaryMoney()
    }

    override fun addEvent() {

    }

    fun updateData(listOrder: ArrayList<DetailOrder>, billNo: String, priceAfterApplyPromotion: Double, discountValue: Double) {
        lstOrder.visibility = View.VISIBLE
        sumMoney = priceAfterApplyPromotion
        discount = discountValue
        listOrderFood.clear()
        listOrderFood.addAll(listOrder)
        adapter.notifyDataSetChanged()


        if (billNo != "")
            tvInvoiceNo.text = activityParent.resources.getString(R.string.format_invoice_no_x, billNo)

        bindSummaryMoney()
    }

    private fun bindSummaryMoney() {
        /*if (discount == 0.toDouble()) {
            tvSumOrderWithoutDiscount.visibility = View.VISIBLE
            tvSumOrderWithoutDiscount.text = mContext.resources.getString(R.string.format_sum_x_k_string, ConvertHelper.doubleToMoney(mContext, sumMoney))

            val layoutParams = tvInvoiceNo.layoutParams as RelativeLayout.LayoutParams
            layoutParams.removeRule(RelativeLayout.CENTER_IN_PARENT)
            tvInvoiceNo.requestLayout()

            layoutSummary.visibility = View.GONE

        } else {*/
            tvSumOrderWithoutDiscount.visibility = View.GONE
            tvSumOrderWithoutDiscount.text = mContext.resources.getString(R.string.format_sum_x_k_string, ConvertHelper.doubleToMoney(mContext, sumMoney))

            val layoutParams = tvInvoiceNo.layoutParams as RelativeLayout.LayoutParams
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
            tvInvoiceNo.requestLayout()

            layoutSummary.visibility = View.VISIBLE
            tvSumOrder.text = ConvertHelper.doubleToMoney(mContext, sumMoney)
            tvDiscount.text = ConvertHelper.doubleToMoney(mContext, discount)
            tvFinal.text = ConvertHelper.doubleToMoney(mContext, sumMoney - discount)
    }
}
