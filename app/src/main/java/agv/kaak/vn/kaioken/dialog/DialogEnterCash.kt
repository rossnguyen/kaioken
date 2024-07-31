package agv.kaak.vn.kaioken.dialog

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.fragment.base.BaseDialogFragment
import agv.kaak.vn.kaioken.utils.Constraint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_enter_cash.*
import kotlin.math.roundToInt

class DialogEnterCash() : BaseDialogFragment() {
    var priceAfter = 0.toDouble()
    var callback: OnButtonClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_enter_cash, container, false)
    }

    override fun getData() {
        priceAfter = arguments!!.getDouble(Constraint.DATA_SEND)
    }

    override fun loadData() {
        tvSumMoney.text = activityParent.resources.getString(R.string.format_sum_bill, priceAfter.roundToInt())
        etPayment.hint = "${priceAfter.roundToInt()}"
    }

    override fun addEvent() {
        etPayment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    val change = java.lang.Double.parseDouble(etPayment.text.toString()) - priceAfter
                    etExchange.setText("${change.roundToInt()}")

                } catch (ex: Exception) {
                    etExchange.setText("n/a")
                }

            }
        })

        btnOk.setOnClickListener {
            var paymentMoney = 0.toDouble()
            try {
                paymentMoney = etPayment.text.toString().toDouble()
            } catch (ex: Exception) {
            }

            if (paymentMoney < priceAfter && paymentMoney != 0.toDouble())
                showDialogMessage(null, activityParent.resources.getString(R.string.payment_cash_must_more_than_total_money))
            else
                if (callback != null) {
                    callback!!.onConfirmClick(paymentMoney)
                    dismiss()
                }

        }

        btnCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun listenSocket() {
        //nothing to do
    }

    override fun offSocket() {
        //nothing to do
    }

    interface OnButtonClickListener {
        fun onConfirmClick(money: Double)
        fun onCancelClick()
    }
}