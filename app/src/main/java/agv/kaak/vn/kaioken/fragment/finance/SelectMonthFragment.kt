package agv.kaak.vn.kaioken.fragment.finance

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.MonthAdapter
import agv.kaak.vn.kaioken.fragment.base.BaseFragment_
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_select_month.*
import java.util.*

/**
 * Created by shakutara on 27/12/2017.
 */
class SelectMonthFragment : BaseFragment_() {
    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var callback: SelectMonthFragmentCallback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_select_month, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMonthAdapter()
    }

    private fun initMonthAdapter() {

        val monthList = ArrayList<String>()
        monthList.add("1")
        monthList.add("2")
        monthList.add("3")
        monthList.add("4")
        monthList.add("5")
        monthList.add("6")
        monthList.add("7")
        monthList.add("8")
        monthList.add("9")
        monthList.add("10")
        monthList.add("11")
        monthList.add("12")

        gvMonth.adapter = MonthAdapter(context!!, monthList)

        gvMonth.setOnItemClickListener { parent, view, position, id ->
            callback.getSelectedMonth(monthList[position])
        }

    }

//    ///////////////////

    interface SelectMonthFragmentCallback {
        fun getSelectedMonth(month: String?)
    }

    fun getCallbackSelectMonthFragment(callback: SelectMonthFragmentCallback) {
        this.callback = callback
    }

//    ///////////////////

}