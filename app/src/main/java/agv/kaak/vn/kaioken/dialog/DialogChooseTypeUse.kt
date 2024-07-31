package agv.kaak.vn.kaioken.dialog

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.OrderForm
import agv.kaak.vn.kaioken.entity.define.UseType
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.OrderFormPaidContract
import agv.kaak.vn.kaioken.mvp.presenter.OrderFormPaidPresenter
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.dialog_choose_type_use.*

class DialogChooseTypeUse(val mContext: Context, val pageId: Int) : Dialog(mContext),
        OrderFormPaidContract.View {
    var callBack: OnItemChooseTypeUseClick? = null
    var orderForm: ArrayList<OrderForm> = arrayListOf()
    val orderFormPaidPresenter = OrderFormPaidPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_choose_type_use)
        doGetListOrderFormPaid()
        addEvent()
    }

    private fun setEnableButton(orderForm: ArrayList<OrderForm>) {
        orderForm.forEach {
            //local always show
            /*if(it.alias=="local")
                btnLocal.isEnabled=true*/

            if (it.alias == "book" && it.paymentStatus == 1)
                btnBook.isEnabled = true
            if (it.alias == "delivery" && it.paymentStatus == 1)
                btnDelivery.isEnabled = true
        }
    }

    fun addEvent() {
        if (callBack != null) {
            btnLocal.setOnClickListener {
                /*orderForm.forEach {
                    if (it.alias == "local") {
                        callBack!!.onClick(UseType.LOCAL_HAVE_TABLE)
                        return@setOnClickListener
                    }
                }

                GlobalHelper.showMessage(mContext, mContext.resources.getString(R.string.detail_store_order_form_invalid), true)*/
                callBack!!.onClick(UseType.LOCAL_HAVE_TABLE)
            }

            btnBook.setOnClickListener {
                orderForm.forEach {
                    if (it.alias == "book" && it.paymentStatus == 1) {
                        callBack!!.onClick(UseType.BOOK)
                        return@setOnClickListener
                    }
                }

                GlobalHelper.showMessage(mContext, mContext.resources.getString(R.string.detail_store_order_form_invalid), true)
            }

            btnDelivery.setOnClickListener {
                orderForm.forEach {
                    if (it.alias == "delivery" && it.paymentStatus == 1) {
                        callBack!!.onClick(UseType.DELIVERY)
                        return@setOnClickListener
                    }
                }
                GlobalHelper.showMessage(mContext, mContext.resources.getString(R.string.detail_store_order_form_invalid), true)
            }
        }
    }

    private fun doGetListOrderFormPaid() {
        showLoading()
        orderFormPaidPresenter.getListOrderFormPaid(pageId)
    }

    override fun getListOrderFormPaidSuccess(orderFormPaid: ArrayList<OrderForm>) {
        hideLoading()
        orderForm = orderFormPaid
        setEnableButton(orderFormPaid)
    }

    override fun getListOrderFomrPaidFail(msg: String?) {
        hideLoading()
        if (msg == null || msg.isEmpty())
            GlobalHelper.showMessage(mContext, mContext.resources.getString(R.string.all_error_global), true)
        else
            GlobalHelper.showMessage(mContext, msg, true)
    }

    private fun showLoading(){
        layoutContent.visibility=View.INVISIBLE
        layoutLoading.visibility=View.VISIBLE
    }

    private fun hideLoading(){
        layoutContent.visibility=View.VISIBLE
        layoutLoading.visibility=View.GONE
    }

    interface OnItemChooseTypeUseClick {
        fun onClick(type: Int)
    }
}