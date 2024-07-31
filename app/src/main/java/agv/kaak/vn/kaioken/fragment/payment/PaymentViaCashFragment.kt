package agv.kaak.vn.kaioken.fragment.payment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.response.OrderInfoResponse
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.fragment.process.ProcessFragment
import agv.kaak.vn.kaioken.utils.Constraint
import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentManager
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.fragment_payment_via_cash.*
import kotlin.math.roundToInt

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PaymentViaCashFragment : BaseFragment() {

    var priceAfter: Double = 0.toDouble()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_via_cash, container, false)
    }

    override fun getData() {
        priceAfter = (arguments!!.getSerializable(Constraint.DATA_SEND) as OrderInfoResponse).orderInfo?.detailOrderInfo?.priceAfter!!
    }

    override fun loadData() {
        etPayment.hint = priceAfter.roundToInt().toString()
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
                    if (onPaymentMoneyChangeListener != null)
                        onPaymentMoneyChangeListener!!.onChange(java.lang.Double.parseDouble(etPayment.text.toString()))
                } catch (ex: Exception) {
                    etExchange.setText("n/a")
                    if (onPaymentMoneyChangeListener != null)
                        onPaymentMoneyChangeListener!!.onChange(0.toDouble())
                }

            }
        })
    }

    interface OnPaymentMoneyChangeListener {
        fun onChange(value: Double)
    }

    internal var onPaymentMoneyChangeListener: OnPaymentMoneyChangeListener? = null
}
