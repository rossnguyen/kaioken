package agv.kaak.vn.kaioken.fragment.order


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.activity.HomeActivity
import agv.kaak.vn.kaioken.adapter.ConfirmFoodAdapter
import agv.kaak.vn.kaioken.adapter.ShowMenuItemAdapter
import agv.kaak.vn.kaioken.entity.AddFoodToOrder
import agv.kaak.vn.kaioken.entity.CreateOrder
import agv.kaak.vn.kaioken.entity.DetailOrder
import agv.kaak.vn.kaioken.entity.OrderForm
import agv.kaak.vn.kaioken.entity.define.BottomNavigationCode
import agv.kaak.vn.kaioken.entity.define.DiscountType
import agv.kaak.vn.kaioken.entity.define.UseType
import agv.kaak.vn.kaioken.fragment.base.BaseSocketFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.OrderFormPaidContract
import agv.kaak.vn.kaioken.mvp.presenter.OrderFormPaidPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import agv.kaak.vn.kaioken.utils.implement.OnItemBottomNavigationClickListener
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_confirm_order.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ConfirmOrderFragment : BaseSocketFragment(),
        OrderFormPaidContract.View {

    private var fragmentManageOrdering: ManageOrderingFragment? = null
    lateinit var listOrderFood: ArrayList<DetailOrder>
    val orderFormPaidPresenter = OrderFormPaidPresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragmentManageOrdering()
    }

    override fun addEvent() {
        btnSendOrder.setOnClickListener {
            if (listOrderIsEmpty(listOrderFood)) {
                showDialogMessage(mContext.resources.getString(R.string.order_item_is_null), null)
                return@setOnClickListener
            }

            if (onItemBottomNavigationClickListener != null)
                when (Constraint.TYPE_USE) {
                    UseType.LOCAL_HAVE_TABLE -> {
                        onItemBottomNavigationClickListener!!.onClick(BottomNavigationCode.SEND_ORDER_LOCAL)
                        if (onCreateOrderCallBack != null) {
                            if (!Constraint.IS_ADD_FOOD) {
                                showLayoutLoading()
                                orderFormPaidPresenter.getListOrderFormPaid(Constraint.ID_STORE_ORDERING)
                            } else {
                                val addFoodToOrder = AddFoodToOrder(Constraint.ID_ORDERING, listOrderFood, "")
                                onCreateOrderCallBack!!.onAddFood(addFoodToOrder)
                            }
                        }

                    }
                    UseType.BOOK -> onItemBottomNavigationClickListener!!.onClick(BottomNavigationCode.CONFIRM_BOOK)
                    UseType.DELIVERY -> onItemBottomNavigationClickListener!!.onClick(BottomNavigationCode.CONFIRM_DELIVERY)
                }
        }
        layoutLoading.setOnClickListener {
            //nothing to do
        }
    }

    override fun getData() {
        listOrderFood = arguments!!.getSerializable(OrderFragment.LIST_ORDER_FOOD_SEND) as ArrayList<DetailOrder>
    }

    override fun loadData() {
        initFragmentManageOrdering()
        setTextNavigation()
    }

    override fun listenSocket() {
        //Constraint.BASE_SOCKET?.on("createOrder", onCreateOrderNotyListener)
    }

    override fun offSocket() {
        //Constraint.BASE_SOCKET?.off("createOrder")
    }

    override fun setupComponentStatusSocket(): TextView? {
        return null
    }

    private fun initFragmentManageOrdering() {
        fragmentManageOrdering = ManageOrderingFragment()
        val bundle = Bundle()
        bundle.putSerializable(Constraint.DATA_SEND, listOrderFood)
        fragmentManageOrdering?.arguments = bundle

        mFragmentManager.beginTransaction().replace(R.id.layoutManageOrdering, fragmentManageOrdering).commitAllowingStateLoss()
    }

    private fun setTextNavigation() {
        when (Constraint.TYPE_USE) {
            UseType.LOCAL_HAVE_TABLE -> btnSendOrder.text = activityParent.resources.getString(R.string.choose_food_send_order)
            UseType.BOOK -> btnSendOrder.text = activityParent.resources.getString(R.string.choose_food_enter_info_book)
            UseType.DELIVERY -> btnSendOrder.text = activityParent.resources.getString(R.string.choose_food_enter_info_delivery)
        }
    }

    override fun getListOrderFormPaidSuccess(orderFormPaid: ArrayList<OrderForm>) {
        hideLayoutLoading()
        var isAllowOrderLocal = false
        orderFormPaid.forEach {
            if (it.alias == "local" && it.paymentStatus == 1)
                isAllowOrderLocal = true
        }

        if (isAllowOrderLocal) {
            val orderNew = CreateOrder(Constraint.uid!!.toInt(),
                    "",
                    DiscountType.VALUE,
                    0,
                    listOrderFood,
                    "",
                    "",
                    OrderFragment.TYPE_ORDER_LOCAL,
                    Constraint.ID_STORE_ORDERING,
                    1,
                    Constraint.ID_TABLE_ORDERING,
                    0)
            onCreateOrderCallBack!!.onCreate(orderNew)
        } else {
            AlertDialog.Builder(activityParent)
                    .setTitle(activityParent.resources.getString(R.string.detail_store_order_form_invalid))
                    .setMessage(activityParent.resources.getString(R.string.detail_store_return_home))
                    .setNegativeButton(activityParent.resources.getString(R.string.all_ok), object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            val intent = Intent(activityParent, HomeActivity::class.java)
                            startActivity(intent)
                        }
                    })
                    .setCancelable(false)
                    .show()
        }
    }

    override fun getListOrderFomrPaidFail(msg: String?) {
        hideLayoutLoading()
        if (msg == null || msg.isEmpty())
            GlobalHelper.showMessage(mContext, mContext.resources.getString(R.string.all_error_global), true)
        else
            GlobalHelper.showMessage(mContext, msg, true)
    }

    private fun showLayoutLoading() {
        layoutLoading?.visibility = View.VISIBLE
    }

    private fun hideLayoutLoading() {
        layoutLoading?.visibility = View.GONE
    }

    private fun listOrderIsEmpty(listOrder: ArrayList<DetailOrder>): Boolean {
        listOrder.forEach {
            if (it.numberOrder!! > 0)
                return false
        }
        return true
    }

    var onCreateOrderCallBack: CreateOrderCallBack? = null

    interface CreateOrderCallBack {
        fun onCreate(orderNew: CreateOrder)
        fun onAddFood(addFoodToOrder: AddFoodToOrder)
    }

    var onItemBottomNavigationClickListener: OnItemBottomNavigationClickListener? = null
}
