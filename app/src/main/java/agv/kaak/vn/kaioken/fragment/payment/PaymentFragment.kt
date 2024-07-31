package agv.kaak.vn.kaioken.fragment.payment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.activity.SeeInvoiceDetailActivity
import agv.kaak.vn.kaioken.adapter.ShowListCouponAdapter
import agv.kaak.vn.kaioken.dialog.DialogChooseCoupon
import agv.kaak.vn.kaioken.dialog.DialogEnterCash
import agv.kaak.vn.kaioken.entity.define.DiscountType
import agv.kaak.vn.kaioken.entity.response.OrderInfoResponse
import agv.kaak.vn.kaioken.entity.result.KiOfPageResult
import agv.kaak.vn.kaioken.entity.result.KiOfUserResult
import agv.kaak.vn.kaioken.fragment.base.BaseDialogFragment
import agv.kaak.vn.kaioken.fragment.process.ProcessFragment
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.KiContract
import agv.kaak.vn.kaioken.mvp.presenter.KiPresenter
import agv.kaak.vn.kaioken.utils.BaseSocketEmit
import agv.kaak.vn.kaioken.utils.Constraint
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.widget.TextView
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.fragment_payment.*
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.roundToInt


class PaymentFragment : BaseDialogFragment(), KiContract.View {
    companion object {
        val PAYMENT_TYPE_CASH = 1
        val PAYMENT_TYPE_CARD = 2
        val PAYMENT_TYPE_KI = 4
    }

    private lateinit var progressDialog: ProgressDialog
    private lateinit var paymentViaCashFragment: PaymentViaCashFragment
    private lateinit var paymentViaKiFragment: PaymentViaKiFragment

    private val kiPresenter = KiPresenter(this)

    private var orderId: Int = -1
    private var invoiceNo = ""

    private val baseSocketEmit = BaseSocketEmit()

    lateinit var orderInfo: OrderInfoResponse
    private var oldPriceAfter = 0.toDouble()
    private var oldPriceVat = 0.toDouble()
    private var priceAfterApplyPromotion = 0.toDouble()
    private var paymentMoney = 0.toDouble()
    private var paymentType = PAYMENT_TYPE_CASH
    private var kiPoint = 0.toDouble()
    private var couponCode = ""
    private var priceCoupon = 0.toDouble()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun getData() {
        orderInfo = arguments!!.getSerializable(Constraint.DATA_SEND) as OrderInfoResponse
        orderId = orderInfo.orderInfo?.detailOrderInfo?.orderId!!
        invoiceNo = orderInfo.orderInfo?.detailOrderInfo?.invoiceNo!!
        orderInfo.orderInfo?.detailOrderInfo?.apply {
            if (discountFinalMatch == null)
                discountFinalMatch = 0.toDouble()

            if (totalItemDiscountValue == null)
                totalItemDiscountValue = 0.toDouble()

            if (extraValue == null)
                extraValue = 0.toDouble()

            if (vatValue == null)
                vatValue = 0.toDouble()

            if (vat == null)
                vat = 0

            oldPriceAfter = priceAfter!!
            oldPriceVat = vatValue!!
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //No call for super(). Bug on API Level > 11.
        //must call this, unless crash app
    }

    override fun loadData() {
        //set text for invoice no
        tvNoBill?.text = activityParent.resources.getString(R.string.format_invoice_no_x, invoiceNo)

        orderInfo.orderInfo?.detailOrderInfo?.apply {
            priceAfterApplyPromotion = priceBefore!! - totalItemDiscountValue!! - discountFinalMatch!!
            tvSumMoney?.text = ConvertHelper.doubleToMoney(mContext, priceAfterApplyPromotion)
            //tvDiscount?.text = "0"
            if (extraValue != 0.toDouble()) {
                tvExtra?.text = ConvertHelper.doubleToMoney(mContext, extraValue!!)
                layoutExtra?.visibility = View.VISIBLE
            } else
                layoutExtra?.visibility = View.GONE

            if (vatValue != 0.toDouble()) {
                tvVAT?.text = ConvertHelper.doubleToMoney(mContext, vatValue!!)
                layoutVAT?.visibility = View.VISIBLE
            } else
                layoutVAT?.visibility = View.GONE

            //tvFinal?.text = "${priceAfter?.roundToInt()}"


            val newPriceAfterWithoutVat = priceAfterApplyPromotion - priceCoupon + extraValue!!
            val newVat = newPriceAfterWithoutVat * vat!! / 100
            priceAfter = newPriceAfterWithoutVat + newVat

            tvDiscount.text = ConvertHelper.doubleToMoney(mContext, priceCoupon)
            tvFinal.text = ConvertHelper.doubleToMoney(mContext, priceAfter!!)
            tvVAT.text = ConvertHelper.doubleToMoney(mContext, newVat)
        }

        layoutLoading?.visibility = View.VISIBLE
        kiPresenter.getKiOfPage(Constraint.ID_STORE_ORDERING)
        progressDialog = GlobalHelper.createProgressDialogHandling(activityParent, R.style.progressDialogPrimary)

        /*//hide textview choose coupon if exist discount
        if (discountValueFinalMatch.roundToInt() > 0)
            tvAddCoupon?.visibility = View.GONE
        elset
            tvAddCoupon?.visibility = View.VISIBLE*/
    }

    private fun bindFragmentToView() {
        if (kiPoint < orderInfo.orderInfo?.detailOrderInfo?.priceAfter!!)
            layoutChoosePaymentType?.visibility = View.GONE
        else
            layoutChoosePaymentType?.visibility = View.VISIBLE

        //init fragment
        paymentViaCashFragment = PaymentViaCashFragment()
        paymentViaCashFragment.arguments = arguments!!
        paymentViaCashFragment.onPaymentMoneyChangeListener = object : PaymentViaCashFragment.OnPaymentMoneyChangeListener {
            override fun onChange(value: Double) {
                paymentMoney = value
            }
        }

        paymentViaKiFragment = PaymentViaKiFragment()
        val bundle = Bundle()
        bundle.putDouble(ProcessFragment.PRICE_AFTER_SEND, orderInfo.orderInfo?.detailOrderInfo?.priceAfter!!)
        bundle.putDouble(ProcessFragment.KI_POINT_SEND, kiPoint)
        paymentViaKiFragment.arguments = bundle

        //add fragment to view
        mFragmentManager.beginTransaction()
                .replace(R.id.layoutInfoPaymentContent, paymentViaCashFragment)
                .commitAllowingStateLoss()

    }

    override fun addEvent() {
        btnSendRequirePayment.setOnClickListener {
            when (paymentType) {
                PAYMENT_TYPE_CASH -> sendRequireByCash()
                PAYMENT_TYPE_KI -> sendRequireByKi()
            }
        }

        ibtnClose.setOnClickListener {
            dialog.dismiss()
        }

        btnPaymentViaCash.setOnClickListener {
            mFragmentManager.beginTransaction()
                    .replace(R.id.layoutInfoPaymentContent, paymentViaCashFragment).commitAllowingStateLoss()
            highLightPaymentChoosed(btnPaymentViaCash)
            paymentType = PAYMENT_TYPE_CASH
        }

        btnPaymentViaKi.setOnClickListener {
            mFragmentManager.beginTransaction()
                    .replace(R.id.layoutInfoPaymentContent, paymentViaKiFragment).commitAllowingStateLoss()
            highLightPaymentChoosed(btnPaymentViaKi)
            paymentType = PAYMENT_TYPE_KI
        }

        tvAddCoupon.setOnClickListener {
            showDialogChooseCoupon()
        }

        btnSeeInvoice.setOnClickListener {
            val intentSeeInvoice = Intent(activityParent, SeeInvoiceDetailActivity::class.java)
            intentSeeInvoice.putExtra(Constraint.DATA_SEND, orderInfo.orderInfo?.detailOrderInfo?.invoiceNo)
            startActivity(intentSeeInvoice)
        }
    }

    private fun showDialogChooseCoupon() {
        val dialogChooseCoupon = DialogChooseCoupon(activityParent, Constraint.ID_STORE_ORDERING)
        dialogChooseCoupon.onCouponChoosedListener = object : ShowListCouponAdapter.OnCouponChoosedListener {
            override fun onChoosed(type: Int, value: Int, code: String) {
                if (value > 0) {
                    if (type == DiscountType.PERCENT)
                        tvAddCoupon?.text = activityParent.resources.getString(R.string.payment_use_coupon_x, activityParent.resources.getString(R.string.format_x_percent, value))
                    else
                        tvAddCoupon?.text = activityParent.resources.getString(R.string.payment_use_coupon_x, activityParent.resources.getString(R.string.format_x_k, value))
                    couponCode = code
                } else {
                    tvAddCoupon?.text = activityParent.resources.getString(R.string.payment_enter_discount)
                    couponCode = ""
                }

                orderInfo.orderInfo?.detailOrderInfo?.apply {
                    priceCoupon = if (type == DiscountType.PERCENT) {
                        priceAfterApplyPromotion * value / 100
                    } else
                        value.toDouble()

                    val newPriceAfterWithoutVat = priceAfterApplyPromotion - priceCoupon + extraValue!!
                    val newVat = newPriceAfterWithoutVat * vat!! / 100
                    priceAfter = newPriceAfterWithoutVat + newVat

                    tvDiscount.text = ConvertHelper.doubleToMoney(mContext, priceCoupon)
                    tvFinal.text = ConvertHelper.doubleToMoney(mContext, priceAfter!!)
                    tvVAT.text = ConvertHelper.doubleToMoney(mContext, newVat)

                    arguments!!.putDouble(ProcessFragment.PRICE_AFTER_SEND, priceAfter!!)
                    bindFragmentToView()
                }
            }
        }
        dialogChooseCoupon.show()
    }

    override fun getListKiOfUserSuccess(listKiOfUser: KiOfUserResult) {
        //nothing to do
    }

    override fun getListKiOfUserFail(msg: String?) {
        //nothing to do
    }

    override fun getKiOfPageSuccess(kiOfPage: KiOfPageResult) {
        layoutLoading?.visibility = View.GONE
        kiPoint = kiOfPage.kiPoint!!
        btnPaymentViaKi?.text = ConvertHelper.doubleToKi(mContext,kiPoint)
        bindFragmentToView()
        //addInfoToView()
    }

    override fun getKiOfPageFail(msg: String?) {
        layoutLoading?.visibility = View.GONE

        layoutChoosePaymentType?.visibility = View.GONE
        Log.e("****GetKiOfPage", msg)

        bindFragmentToView()
    }

    private fun sendRequireByCash() {
        showDialogEnterCash()
    }

    private fun showDialogEnterCash() {
        val dialogEnterCash = DialogEnterCash()
        val bundle = Bundle()
        bundle.putDouble(Constraint.DATA_SEND, orderInfo.orderInfo?.detailOrderInfo?.priceAfter!!)
        dialogEnterCash.arguments = bundle

        val onButtonClickListener = object : DialogEnterCash.OnButtonClickListener {
            override fun onConfirmClick(money: Double) {
                paymentMoney = money

                //check status socket before emit
                if (!GlobalHelper.networkIsConnected(activityParent))
                    return

                //show diloag handling

                progressDialog.show()

                var alreadyEmitSocket = false

                object : CountDownTimer(10000L, 1000L) {
                    override fun onFinish() {
                        if (progressDialog.isShowing) {
                            GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.all_error_global), true)
                            progressDialog.dismiss()
                        }
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        if (Constraint.BASE_SOCKET!!.connected() && !alreadyEmitSocket) {
                            //send require payment
                            if (paymentMoney == 0.toDouble()) {
                                baseSocketEmit.requestBilling(orderId, orderInfo.orderInfo?.detailOrderInfo?.priceAfter!!, PAYMENT_TYPE_CASH, couponCode)
                                Constraint.BASE_SOCKET?.on(Socket.EVENT_CONNECT) {
                                    baseSocketEmit.requestBilling(orderId, orderInfo.orderInfo?.detailOrderInfo?.priceAfter!!, PAYMENT_TYPE_CASH, couponCode)
                                }
                            } else {
                                baseSocketEmit.requestBilling(orderId, paymentMoney, PAYMENT_TYPE_CASH, couponCode)
                                Constraint.BASE_SOCKET?.on(Socket.EVENT_CONNECT) {
                                    baseSocketEmit.requestBilling(orderId, paymentMoney, PAYMENT_TYPE_CASH, couponCode)
                                }
                            }
                            alreadyEmitSocket = true
                        }

                    }
                }.start()
            }

            override fun onCancelClick() {
                //nothing to do
            }
        }
        dialogEnterCash.callback = onButtonClickListener

        dialogEnterCash.show(mFragmentManager, "ENTER_CASH")
    }

    private fun sendRequireByKi() {
        //check status socket before emit
        if (!GlobalHelper.networkIsConnected(activityParent))
            return

        //show diloag handling
        progressDialog.show()

        var alreadyEmitSocket = false

        object : CountDownTimer(10000L, 1000L) {
            override fun onFinish() {
                if (progressDialog.isShowing) {
                    GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.all_error_global), true)
                    progressDialog.dismiss()
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                if (Constraint.BASE_SOCKET!!.connected() && !alreadyEmitSocket) {
                    //send require payment
                    baseSocketEmit.requestBilling(orderId, orderInfo.orderInfo?.detailOrderInfo?.priceAfter!!, PAYMENT_TYPE_KI, couponCode)
                    Constraint.BASE_SOCKET?.on(Socket.EVENT_CONNECT) {
                        baseSocketEmit.requestBilling(orderId, orderInfo.orderInfo?.detailOrderInfo?.priceAfter!!, PAYMENT_TYPE_KI, couponCode)
                    }
                    alreadyEmitSocket = true
                }

            }
        }.start()
    }

    override fun listenSocket() {
        Constraint.BASE_SOCKET?.on("customerRequestBilling", onRequestBillingListener)
    }

    override fun offSocket() {
        Constraint.BASE_SOCKET?.off("customerRequestBilling")
        Constraint.BASE_SOCKET?.off(Socket.EVENT_CONNECT)
    }

    private fun highLightPaymentChoosed(btnHighLight: TextView) {
        btnPaymentViaCash.setBackgroundResource(R.drawable.custom_background_5_radius_transparent_border_gray)
        btnPaymentViaKi.setBackgroundResource(R.drawable.custom_background_5_radius_transparent_border_gray)

        //hight light button choosed
        btnHighLight.setBackgroundResource(R.drawable.custom_background_5_radius_violet_light)
    }

    private var onRequestBillingListener: Emitter.Listener = Emitter.Listener { args ->
        activityParent.runOnUiThread {
            Constraint.BASE_SOCKET?.off(Socket.EVENT_CONNECT)
            progressDialog.dismiss()
            val jsonResponse = args[0] as JSONObject
            try {
                val status = jsonResponse.getInt("status")
                if (status == 0)
                    GlobalHelper.showMessage(mContext, jsonResponse.getJSONObject("error").getString("msg"), true)
                else {
                    GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.payment_send_require_payment_success_message), true)
                    dialog.dismiss()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        ProcessFragment.dialogIsShowing = false
    }
}
