package agv.kaak.vn.kaioken.fragment.payment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.fragment.process.ProcessFragment
import kotlinx.android.synthetic.main.fragment_payment_via_ki.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PaymentViaKiFragment : BaseFragment() {

    var priceAfter = 0.toDouble()
    var kiPoint = 0.toDouble()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_via_ki, container, false)
    }

    override fun getData() {
        priceAfter = arguments!!.getDouble(ProcessFragment.PRICE_AFTER_SEND)
        kiPoint = arguments!!.getDouble(ProcessFragment.KI_POINT_SEND)
    }

    override fun loadData() {
        etKiPut.setText("${priceAfter.toInt()}")
        etKiExcess.setText("${(kiPoint - priceAfter).toInt()}")
    }

    override fun addEvent() {
        //nothing to do
    }
}
