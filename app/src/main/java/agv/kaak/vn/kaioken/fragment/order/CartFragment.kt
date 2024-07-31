package agv.kaak.vn.kaioken.fragment.order

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.CartAdapter
import agv.kaak.vn.kaioken.adapter.ShowMenuItemAdapter
import agv.kaak.vn.kaioken.entity.DetailOrder
import agv.kaak.vn.kaioken.fragment.base.BaseBottomSheetDialogFragment
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlin.math.roundToInt

class CartFragment : BaseBottomSheetDialogFragment() {

    private var sourceListOrder: ArrayList<DetailOrder>? = null
    private var adapterListOrder: CartAdapter? = null
    var isExpand = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun dialogIsExpand() = isExpand

    override fun getData() {
        sourceListOrder = arguments?.getSerializable(Constraint.DATA_SEND) as ArrayList<DetailOrder>
    }

    override fun addEvent() {
        btnDone.setOnClickListener {
            DetailOrder.removeEmptyItemFromListOrder(sourceListOrder!!)
            onDismissListener?.onDismiss(dialog)
            dismiss()
        }
    }

    override fun loadData() {
        initListOrdering()
        bindTitle()
    }

    private fun initListOrdering() {
        adapterListOrder = CartAdapter(mContext, sourceListOrder!!)
        adapterListOrder?.onChangeNumberListener = object : ShowMenuItemAdapter.OnChangeNumberListener {
            override fun onChangeNumberOrder(index: Int, oldValue: Int, newValue: Int) {
                bindTitle()
            }
        }

        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        lstOrdering.adapter = adapterListOrder
        lstOrdering.layoutManager = layoutManager
    }

    private fun bindTitle() {
        tvTotalQuantity.text = DetailOrder.getSumQuantity(sourceListOrder!!).toString()
        tvTotalPrice.text = ConvertHelper.doubleToMoney(mContext, DetailOrder.getSumPrice(sourceListOrder!!))
    }


    var onDismissListener: DialogInterface.OnDismissListener? = null
}