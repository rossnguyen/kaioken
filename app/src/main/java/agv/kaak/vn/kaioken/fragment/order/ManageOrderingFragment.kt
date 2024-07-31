package agv.kaak.vn.kaioken.fragment.order


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.ConfirmFoodAdapter
import agv.kaak.vn.kaioken.entity.DetailOrder
import agv.kaak.vn.kaioken.entity.define.DiscountType
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.DialogInterface
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_manage_ordering.*
import kotlinx.android.synthetic.main.layout_delivery_info.*
import kotlin.math.roundToInt

class ManageOrderingFragment : BaseFragment() {

    var adapterOrdering: ConfirmFoodAdapter? = null
    lateinit var sourceOrdering: ArrayList<DetailOrder>
    var pricePromotion = 0.toDouble()
    var priceCoupon = 0.toDouble()
    var priceOrigin = 0.toDouble()
    var pricePromotionAndCoupon = 0.toDouble()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_ordering, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListOrdering()
        updateSumMoney()
    }

    override fun getData() {
        sourceOrdering = arguments!!.getSerializable(Constraint.DATA_SEND) as ArrayList<DetailOrder>
    }

    override fun loadData() {
        //
    }

    override fun addEvent() {
        //
    }

    private fun initListOrdering() {
        adapterOrdering = ConfirmFoodAdapter(mContext, sourceOrdering)
        adapterOrdering?.onItemOrderClickListener = object : ConfirmFoodAdapter.OnItemOrderClickListener {
            override fun onClick(detailOrder: DetailOrder) {
                showDialogCart()
            }
        }

        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        lstOrder.adapter = adapterOrdering
        lstOrder.layoutManager = layoutManager
    }

    private fun showDialogCart() {
        val fragmentCart = CartFragment()

        val bundle = Bundle()
        bundle.putSerializable(Constraint.DATA_SEND, sourceOrdering)
        fragmentCart.arguments = bundle
        fragmentCart.onDismissListener = DialogInterface.OnDismissListener {
            adapterOrdering?.notifyDataSetChanged()
            updateSumMoney()
        }
        fragmentCart.isCancelable = false
        fragmentCart.isExpand = sourceOrdering.size >= 4
        fragmentCart.show(mFragmentManager, fragmentCart.tag)
    }

    private fun updateSumMoney() {
        var sumMoney = 0.toDouble()
        var sumQuantity = 0
        for (eachOrder: DetailOrder in sourceOrdering) {
            sumQuantity += eachOrder.numberOrder!!
            sumMoney += eachOrder.getPrice() * eachOrder.numberOrder!!
        }

        priceOrigin = sumMoney

        tvSumQuantity?.text = activityParent.getString(R.string.format_sum_x_quantity, sumQuantity)
        tvSumMoney?.text = ConvertHelper.doubleToMoney(mContext, priceOrigin)


        if (OrderFragment.isApplyPromotionAllOrder)
            applyPromotion(OrderFragment.storePromotion?.discountType!!, OrderFragment.storePromotion?.discountValue!!)
        else
            updateFinalMoney()
    }

    fun applyCoupon(couponType: Int, couponValue: Int) {
        priceCoupon = if (couponType == DiscountType.VALUE)
            couponValue.toDouble()
        else
            priceOrigin * couponValue / 100

        updatePricePromotionAndCoupon()
    }

    private fun applyPromotion(discountType: Int, discountValue: Int) {
        if (OrderFragment.storePromotion?.priceMin == null)
            OrderFragment.storePromotion?.priceMin = 0.toDouble()

        pricePromotion = if (OrderFragment.storePromotion != null) {
            if (priceOrigin < OrderFragment.storePromotion?.priceMin!!)
                0.toDouble()
            else {
                if (discountType == DiscountType.VALUE)
                    discountValue.toDouble()
                else
                    priceOrigin * discountValue / 100
            }
        } else
            0.toDouble()

        if (OrderFragment.storePromotion != null)
            if (OrderFragment.storePromotion?.valueMax != null)
                if (pricePromotion > OrderFragment.storePromotion?.valueMax!!)
                    pricePromotion = OrderFragment.storePromotion?.valueMax!!

        updatePricePromotionAndCoupon()
    }

    private fun updatePricePromotionAndCoupon() {
        pricePromotionAndCoupon = priceCoupon + pricePromotion
        tvDisCountValue?.text = ConvertHelper.doubleToMoney(mContext, pricePromotionAndCoupon)
        updateFinalMoney()
    }

    private fun updateFinalMoney() {
        val finalMoney = priceOrigin - pricePromotionAndCoupon
        tvFinalMoney?.text = ConvertHelper.doubleToMoney(mContext, finalMoney)
        onFinalMoneyChangeListener?.onChange(finalMoney)
    }

    var onFinalMoneyChangeListener: OnFinalMoneyChangeListener? = null

    interface OnFinalMoneyChangeListener {
        fun onChange(value: Double)
    }
}
