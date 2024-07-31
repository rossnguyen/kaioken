package agv.kaak.vn.kaioken.dialog

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.entity.DetailWallet
import agv.kaak.vn.kaioken.entity.MoneyLimit
import agv.kaak.vn.kaioken.entity.response.wallet.ItemWallet
import agv.kaak.vn.kaioken.fragment.base.BaseDialogFragment_
import agv.kaak.vn.kaioken.fragment.finance.CaldroidSampleCustomFragment
import agv.kaak.vn.kaioken.fragment.finance.FinanceFragment.parseDate
import agv.kaak.vn.kaioken.fragment.finance.SelectMonthFragment
import agv.kaak.vn.kaioken.mvp.contract.FinanceManagementContract
import agv.kaak.vn.kaioken.mvp.presenter.FinanceManagementPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import agv.kaak.vn.kaioken.utils.DateUtils
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.roomorama.caldroid.CaldroidFragment
import com.roomorama.caldroid.CaldroidListener
import kotlinx.android.synthetic.main.dialog_calendar.*
import java.text.SimpleDateFormat
import java.util.*

class DialogCalendar : BaseDialogFragment_(), FinanceManagementContract.View, View.OnClickListener {
    override fun getWalletSuccess(wallet: DetailWallet?) {
        hideLoading()
        if (wallet?.detail == null)
            return

        var splitDate: List<String>?
        wallet.detail?.forEach {
            splitDate = it.date?.split(" ")
            data.put(parseDate(splitDate!![0]), wallet.detail as ArrayList<ItemWallet>)
            caldroidFragment.refreshView()

            caldroidFragment.clearSelectedDates()
            caldroidFragment.setSelectedDate(currentDate)
        }
    }

    override fun getWalletFailed(msg: String?) {
        hideLoading()
        showMessage(msg)
    }


    override fun onClick(v: View?) {
        when (v) {
            tvPreviewMode -> changePreviewMode()
        }
    }

    private fun changePreviewMode() {
        if (!isPreviewAsDay) {
            showHeaderTitle(DateUtils.convertDateToMonthYearString(currentDate))
            layout_calendar_dialog?.visibility = View.VISIBLE
            viewpagerMonthDialog.visibility = View.GONE
            tvPreviewMode?.text = getString(R.string.finance_watch_via_month)
            isPreviewAsDay = true
        } else {
            showHeaderTitle("$currentYear")
            layout_calendar_dialog?.visibility = View.GONE
            viewpagerMonthDialog?.visibility = View.VISIBLE
            tvPreviewMode?.text = getString(R.string.finance_watch_via_day)
            isPreviewAsDay = false
        }
    }

    override fun showLoading() {
        loader?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loader?.visibility = View.GONE
    }

    override fun getWalletListViaTypeSuccess(itemWalletList: List<ItemWallet>?) {}

    override fun getWalletListViaTypeFailed(msg: String?) {}

    override fun saveWalletLimitSuccess(moneyLimit: MoneyLimit?) {}

    override fun saveWalletLimitFailed(msg: String?) {}

    override fun saveWalletSuccess(itemWallet: ItemWallet?) {}

    override fun saveWalletFailed(msg: String?) {}

    override fun deleteMoneySuccess(itemWallet: ItemWallet?) {}

    override fun deleteMoneyFailed(msg: String?) {}

    private var listener: CaldroidListener = object : CaldroidListener() {
        private var previousView: View? = null

        override fun onSelectDate(date: Date, view: View) {
            val dateText = sdf.format(date)
            callback.getSelectedDate(dateText)
            dismiss()
        }

        override fun onChangeMonth(month: Int, year: Int) {
            showHeaderTitle("$month/$year")
            getDataForRedNote("$year-$month")
        }

        override fun onLongClickDate(date: Date?, view: View?) {

        }

        override fun onCaldroidViewCreated() {
        }

    }

    private val FINANCE_CUSTOMER = 1
    private val FINANCE_CASHIER = 2
    private var isPreviewAsDay = true
    private var currentDate = Calendar.getInstance().time
    private var currentYear = SimpleDateFormat("yyyy").format(currentDate)?.toInt()
    private var cal = Calendar.getInstance()
    private var caldroidFragment = CaldroidSampleCustomFragment()
    private var data = HashMap<Date, ArrayList<ItemWallet>>()
    private val sdf = SimpleDateFormat("yyyy-MM-dd")

    private val financeManagementPresenter = FinanceManagementPresenter(this)
    private lateinit var callback: DialogCalendarListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showHeaderTitle(getCurrentDateText())

        //  get data for red node
        val dateText = SimpleDateFormat("yyyy-MM").format(currentDate)
        getDataForRedNote(dateText)

        showCalendar()
        caldroidFragment.caldroidListener = listener
        initMonthAdapter()

        //  views click
        tvPreviewMode?.setOnClickListener(this)
    }

    private fun getDataForRedNote(dateText: String) {
        showLoading()
        /*financeManagementPresenter.getWallet(dateText, FINANCE_CASHIER,
                Constraint.STORE_WORKING_ID?.toInt(), 0, 1000)*/
        financeManagementPresenter.getWallet(dateText, FINANCE_CUSTOMER,
                Constraint.STORE_WORKING_ID?.toInt(), 0, 1000)
    }

    @SuppressLint("SimpleDateFormat")
    private fun initMonthAdapter() {
        var tempPosition = 500

        val mPagerAdapter = SelectMonthAdapter(childFragmentManager)
        viewpagerMonthDialog?.adapter = mPagerAdapter
        viewpagerMonthDialog?.setCurrentItem(500, true)

        viewpagerMonthDialog?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (position > tempPosition) {
                    currentYear = currentYear?.plus(1)
                    showHeaderTitle("$currentYear")
                }
                if (position < tempPosition) {
                    currentYear = currentYear?.minus(1)
                    showHeaderTitle("$currentYear")
                }
                tempPosition = position
            }
        })
    }

    private fun showCalendar() {

        val args = Bundle()

        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1)
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR))
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY)
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false)
        args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, false)
        caldroidFragment.arguments = args

        childFragmentManager.beginTransaction()
                ?.replace(R.id.layout_calendar_dialog, caldroidFragment)
                ?.commit()

        //  init custom data to CalDroid
        val extraData = caldroidFragment.extraData
        extraData.put("DATA_CALDROID", data)
        caldroidFragment.refreshView()

    }

    private fun getCurrentDateText(): String {
        val currentDate = Calendar.getInstance().time
        return DateUtils.convertDateToStringShort(currentDate)!!
    }

    inner class SelectMonthAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        private var LOOPS_COUNT = 1000

        override fun getItem(position: Int): Fragment {
            val selectMonthFragment = SelectMonthFragment()
            selectMonthFragment.getCallbackSelectMonthFragment(object : SelectMonthFragment.SelectMonthFragmentCallback {
                override fun getSelectedMonth(month: String?) {
                    callback.getSelectedDate("$currentYear-$month")
                    dismiss()
                }
            })
            return selectMonthFragment
        }

        override fun getCount(): Int {
            return LOOPS_COUNT
        }
    }

//  ///////////////////

    interface DialogCalendarListener {
        fun getSelectedDate(dateText: String)
    }

    fun getDialogCalendarListener(callback: DialogCalendarListener) {
        this.callback = callback
    }

//  ///////////////////
}