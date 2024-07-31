package agv.kaak.vn.kaioken.fragment.process


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.activity.HomeActivity
import agv.kaak.vn.kaioken.activity.ModeOfUseActivity
import agv.kaak.vn.kaioken.activity.OrderActivity
import agv.kaak.vn.kaioken.activity.SeeInvoiceDetailActivity
import agv.kaak.vn.kaioken.dialog.DialogRatingAfterPayment
import agv.kaak.vn.kaioken.entity.DetailOrder
import agv.kaak.vn.kaioken.entity.define.DiscountType
import agv.kaak.vn.kaioken.entity.define.OrderStatus
import agv.kaak.vn.kaioken.entity.define.UpdateRealtime2Define
import agv.kaak.vn.kaioken.entity.define.UseType
import agv.kaak.vn.kaioken.entity.response.OrderInfoResponse
import agv.kaak.vn.kaioken.entity.response.SocketRealtimeSurchangeDiscountVat
import agv.kaak.vn.kaioken.fragment.base.BaseSocketFragment
import agv.kaak.vn.kaioken.fragment.payment.PaymentFragment
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.utils.BaseSocketEmit
import agv.kaak.vn.kaioken.utils.Constraint
import android.app.AlertDialog
import android.content.Intent
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.google.gson.Gson
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.fragment_process.*
import org.json.JSONObject
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 *
 */
class ProcessFragment : BaseSocketFragment() {
    companion object {
        val LIST_ORDER_SEND = "LIST_ORDER_SEND"
        val INVOICE_NO_SEND = "INVOICE_NO_SEND"
        val SUM_MONEY_SEND = "SUM_MONEY_SEND"
        val DISCOUNT_SEND = "DISCOUNT_SEND"
        val EXTRA_VALUE_SEND = "EXTRA_VALUE_SEND"
        val VAT_VALUE_SEND = "VAT_VALUE_SEND"
        val PRICE_AFTER_SEND = "PRICE_AFTER_SEND"
        val KI_POINT_SEND = "KI_POINT_SEND"

        val STATUS_SEND = "STATUS_SEND"
        val USE_TYPE_SEND = "USE_TYPE_SEND"

        var dialogIsShowing = false
    }

    var detailOrderFragment: DetailOrderFragment? = null
    var trackingProcessFragment: TrackProcessFragment? = null
    var countDownFragment: CountDownFragment? = null

    lateinit var orderInfo: OrderInfoResponse
    var orderId: Int = -1
    var pageId: Int = -1
    var pageName: String = ""
    var listOrderFood: ArrayList<DetailOrder> = arrayListOf()
    var typeUpdateRealtime = -1

    //data for dialog send require payment
    /*var priceBefore = 0.toDouble()
    var priceAfter = 0.toDouble()
    var discountValueFinalMatch = 0.toDouble()
    var vatValue = 0.toDouble()
    var extraValue = 0.toDouble()
    var invoiceNo = ""*/

    //data for tracking status
    /*var customerName: String = ""
    var staffName: String = ""
    var chefName: String = ""
    var cashierName: String = ""*/

    val baseSocketEmit = BaseSocketEmit()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_process, container, false)
    }

    override fun getData() {
        orderId = arguments!!.getInt(Constraint.ORDER_ID_SEND)
        pageId = Constraint.ID_STORE_ORDERING
        pageName = Constraint.NAME_STORE_ORDERING
    }

    override fun loadData() {
        if (detailOrderFragment == null) {
            //init fragment
            initTrackingProcessFragment(1)
            initFragmentCountDown()
            initFragmentDetailOrder("", listOrderFood, 0.toDouble())

            //add fragment to view
            mFragmentManager.beginTransaction()
                    .replace(R.id.layoutContentTracking, trackingProcessFragment).commit()
            mFragmentManager.beginTransaction()
                    .replace(R.id.layoutContentCountDown, countDownFragment).commit()

            mFragmentManager.beginTransaction()
                    .replace(R.id.layoutContentDetailOrder, detailOrderFragment).commit()

        }

        //load data
        handleSocketToGetOrderInfo()
    }

    override fun addEvent() {
        //init bottom navigation
        initBottomNavigation()

        layoutBottomNavigate.setOnTabSelectedListener { position, wasSelected ->
            when (position) {
                0 -> GlobalHelper.doCallWaiter(activityParent)

                1 -> if (GlobalHelper.networkIsConnected(activityParent)) {
                    Constraint.IS_ADD_FOOD = true
                    Constraint.ID_ORDERING = orderId
                    startActivity(Intent(activityParent, OrderActivity::class.java))
                }

                2 -> showDialogPayment()
            }
            return@setOnTabSelectedListener false
        }

        layoutBottomNavigateForRequestPayment.setOnTabSelectedListener { position, wasSelected ->
            when (position) {
                0 -> GlobalHelper.doCallWaiter(activityParent)
                1 -> showDialogMessage("", activityParent.resources.getString(R.string.payment_order_is_request_billing_can_not_add_food))
                2 -> showBill()
            }

            return@setOnTabSelectedListener false
        }

        ibtnBack.setOnClickListener {
            if (Constraint.loginType == 1)
                startActivity(Intent(activityParent, HomeActivity::class.java))
            else
                startActivity(Intent(activityParent, ModeOfUseActivity::class.java))
        }
    }

    override fun listenSocket() {
        Constraint.BASE_SOCKET?.on("getOrderAndRelateInfoS2", onGetOrderAndRelateListener)
        Constraint.BASE_SOCKET?.on("updateRealtime2", onUpdateOrderRealTime)
        Log.d("****on", "co do ON, trang thai socket: ${Constraint.BASE_SOCKET!!.connected()}")
    }

    override fun offSocket() {
        Constraint.BASE_SOCKET?.off("getOrderAndRelateInfoS2")
        Constraint.BASE_SOCKET?.off("updateRealtime2")
    }

    override fun setupComponentStatusSocket(): TextView {
        return tvStatusSocket
    }

    private fun handleSocketToGetOrderInfo() {
        Log.d("orderId", orderId.toString() + "")
        if (Constraint.BASE_SOCKET!!.connected())
            baseSocketEmit.getOrderAndRelateInfo(orderId)
        else
            Handler().postDelayed({
                if (Constraint.BASE_SOCKET!!.connected()) baseSocketEmit.getOrderAndRelateInfo(orderId)
            }, 2000)
    }

    private val onGetOrderAndRelateListener = Emitter.Listener { args ->
        activityParent.runOnUiThread {
            Log.d("****getOrder&RelateInfo", args[0].toString())
            val jsonObjectResponse = args[0] as JSONObject
            val gson = Gson()
            orderInfo = gson.fromJson<OrderInfoResponse>(jsonObjectResponse.toString(), OrderInfoResponse::class.java)

            if (orderInfo.status == 1) {
                //data for countdown fragment
                val status = orderInfo.orderInfo!!.detailOrderInfo!!.statusOrder

                if (orderInfo.orderInfo!!.detailOrderInfo!!.timeChefConfirm != null) {
                    val timeCheftConfirm = ConvertHelper.stringGlobalToDateTimeWithoutTimezone(orderInfo.orderInfo!!.detailOrderInfo!!.timeChefConfirm!!)
                    if (status == OrderStatus.CHEF_START && timeCheftConfirm != null) {
                        countDownFragment!!.updateRealTimeForProcess(timeCheftConfirm, orderInfo.orderInfo!!.detailOrderInfo!!.intervalChefTime!!)
                    }
                }
                countDownFragment!!.updateSumMoney(orderInfo.orderInfo?.detailOrderInfo?.priceBefore!!)

                //set text for textview summoney
                orderInfo.orderInfo?.detailOrderInfo?.apply {
                    if (discountFinalMatch == null)
                        discountFinalMatch = 0.toDouble()
                    if (totalItemDiscountValue == null)
                        totalItemDiscountValue = 0.toDouble()
                    val priceAfterApplyPromotion = priceBefore!! - discountFinalMatch!! - totalItemDiscountValue!!
                    tvTotalMoney.text = activityParent.resources.getString(R.string.format_sum_x_k, priceAfterApplyPromotion.toInt())
                }

                //data for tab detail tracking
                //show dialog rating if end order
                if (status == OrderStatus.FINISH) {
                    //show dialog after finish order
                    showDialogRating()

                    //clear data
                    Constraint.ID_STORE_ORDERING = -1
                    Constraint.NAME_STORE_ORDERING = ""
                    Constraint.ADDRESS_STORE_ORDERING = ""
                    Constraint.ID_TABLE_ORDERING = -1
                }

                if (status == OrderStatus.CANCEL)
                    showDialogCancelOrder()

                //update step
                trackingProcessFragment!!.setUseType(orderInfo.orderInfo?.detailOrderInfo?.orderType)
                updateStatusTracking(status!!, orderInfo)

                //data for tab detail order
                val billNo = orderInfo.orderInfo!!.detailOrderInfo!!.invoiceNo
                listOrderFood = orderInfo.orderInfo!!.listItem as ArrayList<DetailOrder>

                orderInfo.orderInfo!!.detailOrderInfo!!.apply {
                    if (discountFinalMatch == null)
                        discountFinalMatch = 0.toDouble()
                    if (totalItemDiscountValue == null)
                        totalItemDiscountValue = 0.toDouble()
                    val priceAfterApplyPromotion = priceBefore!! - totalItemDiscountValue!!
                    detailOrderFragment!!.updateData(listOrderFood, billNo!!, priceAfterApplyPromotion, discountFinalMatch!!)
                }

                //update title
                try {
                    //if order is book will not exist table
                    if (orderInfo.orderInfo?.detailOrderInfo?.listTableName != null)
                        tvTitle.text = activityParent.resources.getString(R.string.process_order_name_and_table, "${Constraint.NAME_STORE_ORDERING}", "${orderInfo.orderInfo?.detailOrderInfo?.listTableName}")
                    else
                        tvTitle.text = "${Constraint.NAME_STORE_ORDERING}"
                } catch (ex: Exception) {
                    tvTitle.text = "${Constraint.NAME_STORE_ORDERING}"
                }

                //update table id and store id to constraint
                if (orderInfo.orderInfo?.detailOrderInfo?.tableId != null)
                    Constraint.ID_TABLE_ORDERING = orderInfo.orderInfo?.detailOrderInfo?.tableId!!
                if (orderInfo.orderInfo?.detailOrderInfo?.pageId != null)
                    Constraint.ID_STORE_ORDERING = orderInfo.orderInfo?.detailOrderInfo?.pageId!!

                //hide layout bottom navigate if delivery or (book and not exist table)
                if (orderInfo.orderInfo?.detailOrderInfo?.orderType == UseType.DELIVERY)
                    hideBottomNavigation()
                else if (orderInfo.orderInfo?.detailOrderInfo?.orderType == UseType.BOOK && orderInfo.orderInfo?.detailOrderInfo?.listTableName == null)
                    hideBottomNavigation()
                //if local have table
                else {
                    if (status == OrderStatus.REQUIRE_PAYMENT)
                        displayBottomNavigationForRequestBilling()
                    else
                        displayBottomNavigateNomal()
                }


            } else
                GlobalHelper.showMessage(context, activityParent.resources.getString(R.string.process_get_list_order_fail), false)

        }
    }

    private fun displayBottomNavigateNomal() {
        layoutBottomNavigate.visibility = View.VISIBLE
        layoutBottomNavigateForRequestPayment.visibility = View.GONE
    }

    private fun hideBottomNavigation() {
        layoutBottomNavigate.visibility = View.GONE
        layoutBottomNavigateForRequestPayment.visibility = View.GONE
    }

    private fun displayBottomNavigationForRequestBilling() {
        layoutBottomNavigateForRequestPayment.visibility = View.VISIBLE
        layoutBottomNavigate.visibility = View.GONE
    }

    private val onUpdateOrderRealTime = object : Emitter.Listener {
        override fun call(vararg args: Any) {
            activityParent.runOnUiThread {
                try {
                    Log.d("****updateRealtime2", args[0].toString())
                    val jsonResponse = args[0] as JSONObject
                    typeUpdateRealtime = jsonResponse.getInt("type")
                    when (typeUpdateRealtime) {
                        UpdateRealtime2Define.CONFIRM_ORDER -> {
                            orderInfo.orderInfo?.staffInfo?.name = jsonResponse.getJSONObject("data")
                                    .getJSONObject("customer")
                                    .getJSONObject("staff_confirm")
                                    .getString("name")
                            updateStatusTracking(OrderStatus.CONFIRM, orderInfo)
                            GlobalHelper.ringTheBell(activityParent)
                        }

                        UpdateRealtime2Define.REQUIRE_PAYMENT -> {
                            updateStatusTracking(OrderStatus.REQUIRE_PAYMENT, orderInfo)
                        }

                        UpdateRealtime2Define.CONFIRM_PAYMENT_FROM_CASHIER -> {
                            orderInfo.orderInfo?.chefInfo?.name = jsonResponse.getJSONObject("data")
                                    .getJSONObject("all")
                                    .getJSONObject("cashier_confirm")
                                    .getString("name")
                            updateStatusTracking(OrderStatus.REQUIRE_PAYMENT, orderInfo)

                            //show dialog after finish order
                            showDialogRating()

                            //clear data
                            Constraint.ID_STORE_ORDERING = -1
                            Constraint.NAME_STORE_ORDERING = ""
                            Constraint.ADDRESS_STORE_ORDERING = ""
                            Constraint.ID_TABLE_ORDERING = -1

                            GlobalHelper.ringTheBell(activityParent)
                        }

                        UpdateRealtime2Define.CHEF_START -> {
                            orderInfo.orderInfo?.chefInfo?.name = jsonResponse.getJSONObject("data")
                                    .getJSONObject("all")
                                    .getJSONObject("chef_info")
                                    .getString("name")
                            val intervalChefTime = jsonResponse.getJSONObject("data")
                                    .getJSONObject("all")
                                    .getJSONObject("order_detail")
                                    .getInt("interval_chef_time")
                            val dateChefConfirm = ConvertHelper.stringGlobalToDateTimeWithoutTimezone(
                                    jsonResponse.getJSONObject("data")
                                            .getJSONObject("all")
                                            .getJSONObject("order_detail")
                                            .getString("date_chef_confirm"))
                            updateStatusTracking(OrderStatus.CHEF_START, orderInfo)
                            countDownFragment!!.updateRealTimeForProcess(dateChefConfirm!!, intervalChefTime)
                            GlobalHelper.ringTheBell(activityParent)
                        }

                        UpdateRealtime2Define.CHEF_FINISH -> {
                            updateStatusTracking(OrderStatus.SERVING, orderInfo)
                            GlobalHelper.ringTheBell(activityParent)
                        }

                        UpdateRealtime2Define.SHIPPING -> {
                            updateStatusTracking(OrderStatus.SHIPPING, orderInfo)
                            GlobalHelper.ringTheBell(activityParent)
                        }

                        UpdateRealtime2Define.ADD_OR_REMOVE_FOOD -> {
                            //data for tab detail order
                            listOrderFood.clear()
                            val listItem = jsonResponse.getJSONObject("data")
                                    .getJSONObject("all")
                                    .getJSONArray("list_item")
                            for (i in 0 until listItem.length()) {
                                val eachItem = listItem.getJSONObject(i)
                                listOrderFood.add(DetailOrder.fromJsonObject(eachItem))
                            }

                            orderInfo.orderInfo?.detailOrderInfo?.priceAfter = jsonResponse.getJSONObject("data")
                                    .getJSONObject("all")
                                    .getJSONObject("order_detail")
                                    .getInt("price_after").toDouble()

                            orderInfo.orderInfo?.detailOrderInfo?.priceBefore = jsonResponse.getJSONObject("data")
                                    .getJSONObject("all")
                                    .getJSONObject("order_detail")
                                    .getInt("price_before").toDouble()

                            //update sumbill
                            orderInfo.orderInfo!!.detailOrderInfo!!.apply {
                                if (discountFinalMatch == null)
                                    discountFinalMatch = 0.toDouble()
                                if (totalItemDiscountValue == null)
                                    totalItemDiscountValue = 0.toDouble()
                                val priceAfterApplyPromotion = priceBefore!! - totalItemDiscountValue!!
                                detailOrderFragment!!.updateData(listOrderFood, "", priceAfterApplyPromotion, discountFinalMatch!!)
                            }

                            //update total money above clock
                            countDownFragment!!.updateSumMoney(orderInfo.orderInfo?.detailOrderInfo?.priceAfter!!)
                            tvTotalMoney.text = activityParent.resources.getString(R.string.format_sum_x_k, orderInfo.orderInfo?.detailOrderInfo?.priceBefore!!.toInt())

                            GlobalHelper.ringTheBell(activityParent)
                        }

                        UpdateRealtime2Define.CHANGE_TABLE -> {
                            val newTableId = jsonResponse.getJSONObject("data")
                                    .getJSONObject("all")
                                    .getJSONArray("list_new_table").getInt(0)

                            val newTableName = jsonResponse.getJSONObject("data")
                                    .getJSONObject("all")
                                    .getString("string_new_table_name")

                            Constraint.ID_TABLE_ORDERING = newTableId
                            tvTitle.text = activityParent.resources.getString(R.string.process_order_name_and_table,
                                    Constraint.NAME_STORE_ORDERING,
                                    newTableName)

                            GlobalHelper.ringTheBell(activityParent)
                        }

                        UpdateRealtime2Define.ADD_SURCHARGE_OR_DISCOUNT_OR_VAT -> {
                            val orderDetail = jsonResponse.getJSONObject("data")
                                    .getJSONObject("all")
                                    .getJSONObject("order_detail")
                            val data = SocketRealtimeSurchangeDiscountVat.fromJsonObject(orderDetail)

                            orderInfo.orderInfo?.detailOrderInfo?.priceBefore = data.priceBefore
                            orderInfo.orderInfo?.detailOrderInfo?.priceAfter = data.priceAfter
                            orderInfo.orderInfo?.detailOrderInfo?.extraValue = if (data.extraValue == null) 0.toDouble() else data.extraValue
                            orderInfo.orderInfo?.detailOrderInfo?.vat = if (data.vat == null) 0 else data.vat
                            orderInfo.orderInfo?.detailOrderInfo?.discountType = data.discountType
                            orderInfo.orderInfo?.detailOrderInfo?.discountValue = if (data.discountValue == null) 0.toDouble() else data.discountValue
                            if (orderInfo.orderInfo?.detailOrderInfo?.discountType == DiscountType.PERCENT)
                                orderInfo.orderInfo?.detailOrderInfo?.discountFinalMatch = (data.priceBefore!! + orderInfo.orderInfo?.detailOrderInfo?.extraValue!!) * orderInfo.orderInfo?.detailOrderInfo?.discountValue!! / 100
                            else
                                orderInfo.orderInfo?.detailOrderInfo?.discountFinalMatch = orderInfo.orderInfo?.detailOrderInfo?.discountValue
                            orderInfo.orderInfo?.detailOrderInfo?.discountType = data.discountType
                            orderInfo.orderInfo?.detailOrderInfo?.discountName = data.discountName
                            orderInfo.orderInfo?.detailOrderInfo?.vatValue = orderInfo.orderInfo?.detailOrderInfo?.priceAfter!! * (1 - 1 / (1 + orderInfo.orderInfo?.detailOrderInfo?.vat!! / 100f))

                            /*//discountValueFinalMatch = data.getDouble("discount_value")
                            orderInfo.orderInfo?.detailOrderInfo?.discountValueFinalMatch = priceBefore + extraValue + vatValue - priceAfter*/
                        }

                        UpdateRealtime2Define.CANCEL_ORDER -> showDialogCancelOrder()
                    }
                } catch (ex: Exception) {
                    GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.something_went_wrong), true)
                    Log.e("error", ex.message)
                }
            }
        }
    }

    private fun updateStatusTracking(status: Int, orderInfo: OrderInfoResponse) {
        trackingProcessFragment!!.updateStatus(
                status,
                orderInfo.orderInfo!!.userInfo!!.name!!,
                if (orderInfo.orderInfo?.staffInfo != null) orderInfo.orderInfo?.staffInfo?.name!! else "",
                if (orderInfo.orderInfo?.chefInfo != null) orderInfo.orderInfo?.chefInfo?.name!! else "",
                if (orderInfo.orderInfo?.cashierInfo != null) orderInfo.orderInfo?.cashierInfo?.name!! else "")

    }

    private fun initBottomNavigation() {
        //setup layout bottom navigate before request billing
        layoutBottomNavigate.addItem(AHBottomNavigationItem(activityParent.resources.getString(R.string.all_call_waiter), activityParent.getDrawable(R.drawable.ic_bell)))
        layoutBottomNavigate.addItem(AHBottomNavigationItem(activityParent.resources.getString(R.string.all_add_food), activityParent.getDrawable(R.drawable.ic_add_food)))
        layoutBottomNavigate.addItem(AHBottomNavigationItem(activityParent.resources.getString(R.string.all_payment), activityParent.getDrawable(R.drawable.ic_cash)))

        layoutBottomNavigate.defaultBackgroundColor = ContextCompat.getColor(activityParent, R.color.colorMasterialGrey_4)
        layoutBottomNavigate.currentItem = -1

        // setup layout bottom navigate when status is request billing
        layoutBottomNavigateForRequestPayment.addItem(AHBottomNavigationItem(activityParent.resources.getString(R.string.all_call_waiter), activityParent.getDrawable(R.drawable.ic_bell)))
        layoutBottomNavigateForRequestPayment.addItem(AHBottomNavigationItem(activityParent.resources.getString(R.string.all_add_food), activityParent.getDrawable(R.drawable.ic_add_food)))
        layoutBottomNavigateForRequestPayment.addItem(AHBottomNavigationItem(activityParent.resources.getString(R.string.payment_see_invoice), activityParent.getDrawable(R.drawable.ic_bill)))


        layoutBottomNavigateForRequestPayment.defaultBackgroundColor = ContextCompat.getColor(activityParent, R.color.colorMasterialGrey_4)
        layoutBottomNavigateForRequestPayment.currentItem = -1
    }

    private fun initTrackingProcessFragment(status: Int) {
        trackingProcessFragment = TrackProcessFragment()
        val bundle = Bundle()
        bundle.putInt(STATUS_SEND, status)
        trackingProcessFragment!!.arguments = bundle
    }

    private fun initFragmentDetailOrder(invoiceNo: String, listOrder: ArrayList<DetailOrder>, discount: Double) {

        detailOrderFragment = DetailOrderFragment()
        val bundle = Bundle()
        bundle.putSerializable(LIST_ORDER_SEND, listOrder)
        bundle.putString(INVOICE_NO_SEND, invoiceNo)
        bundle.putDouble(SUM_MONEY_SEND, 0.toDouble())
        bundle.putDouble(DISCOUNT_SEND, discount)
        detailOrderFragment!!.arguments = bundle
    }

    private fun initFragmentCountDown() {
        countDownFragment = CountDownFragment()
    }

    fun showDialogRating() {
        val dialogRatingAfterPayment = DialogRatingAfterPayment(activityParent,
                pageId,
                pageName,
                orderId)
        dialogRatingAfterPayment.setCancelable(true)
        dialogRatingAfterPayment.show()
        dialogRatingAfterPayment.setOnDismissListener { dialog ->
            dialogIsShowing = false
            startActivity(Intent(activityParent, HomeActivity::class.java))
        }
        dialogIsShowing = true
    }

    private fun showDialogPayment() {
        val paymentFragment = PaymentFragment()
        val bundle = Bundle()
        bundle.putSerializable(Constraint.DATA_SEND, orderInfo)
        paymentFragment.arguments = bundle

        paymentFragment.show(mFragmentManager, "PAYMENT")
        dialogIsShowing = true
    }

    fun showDialogCancelOrder() {
        AlertDialog.Builder(activityParent)
                .setMessage(activityParent.resources.getString(R.string.process_order_cancel_message))
                .setPositiveButton(activityParent.resources.getString(R.string.all_return_home)) { dialog, which ->
                    dialog.dismiss()
                    dialogIsShowing = false
                    startActivity(Intent(activityParent, HomeActivity::class.java))
                }
                .setCancelable(false)
                .show()
        dialogIsShowing = true
    }

    private fun showBill() {
        val intentSeeInvoice = Intent(activityParent, SeeInvoiceDetailActivity::class.java)
        intentSeeInvoice.putExtra(Constraint.DATA_SEND, orderInfo.orderInfo?.detailOrderInfo?.invoiceNo)
        startActivity(intentSeeInvoice)
    }
}
