package agv.kaak.vn.kaioken.fragment.order


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.ConfirmFoodAdapter
import agv.kaak.vn.kaioken.adapter.ShowMenuItemAdapter
import agv.kaak.vn.kaioken.dialog.DialogAddText
import agv.kaak.vn.kaioken.dialog.DialogChooseNumber
import agv.kaak.vn.kaioken.entity.CreateBook
import agv.kaak.vn.kaioken.entity.DetailOrder
import agv.kaak.vn.kaioken.entity.define.BottomNavigationCode
import agv.kaak.vn.kaioken.entity.define.DiscountType
import agv.kaak.vn.kaioken.entity.result.DetailCoupon
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.DetailCouponContract
import agv.kaak.vn.kaioken.mvp.presenter.DetailCouponPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import agv.kaak.vn.kaioken.utils.implement.OnItemBottomNavigationClickListener
import agv.kaak.vn.kaioken.widget.InputText
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import kotlinx.android.synthetic.main.fragment_info_book.*
import kotlinx.android.synthetic.main.layout_book_info.*
import kotlinx.android.synthetic.main.layout_order_info.*
import java.util.*

class InfoBookFragment : BaseFragment(){

    private lateinit var listOrderFood: ArrayList<DetailOrder>
    private var fragmentManageOrdering: ManageOrderingFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragmentManageOrdering()
    }

    override fun getData() {
        listOrderFood = arguments!!.getSerializable(OrderFragment.LIST_ORDER_FOOD_SEND) as ArrayList<DetailOrder>
    }

    override fun loadData() {

        //updateSumMoney()
    }

    override fun addEvent() {
        btnConfirm.setOnClickListener {
            if (listOrderIsEmpty(listOrderFood)) {
                showDialogMessage(mContext.resources.getString(R.string.order_item_is_null), null)
                return@setOnClickListener
            }

            if (checkValidData()) {
                if (onItemBottomNavigationClickListener != null)
                    onItemBottomNavigationClickListener!!.onClick(BottomNavigationCode.CONFIRM_BOOK)
                if (onCreateBookCallBack != null) {
                    val timeArrival = ConvertHelper.stringToDateTime("${tvArrivalTime.text}:00")
                    val bookNew = CreateBook(Constraint.uid!!,
                            Constraint.phoneUser,
                            "",
                            tvNote.text.toString(),
                            tvCountCustomer.text.toString(),
                            timeArrival,
                            OrderFragment.TYPE_ORDER_BOOK.toString(),
                            Constraint.ID_STORE_ORDERING.toString(),
                            "1",
                            "hello",
                            DiscountType.VALUE.toString(),
                            "0",
                            "abcd@gmail.com",
                            listOrderFood)
                    onCreateBookCallBack!!.onCreate(bookNew)
                }
            }
        }

        layoutArrivalTime.setOnClickListener {
            var oldHour = 0
            var oldMinutes = 0
            var oldDate = Calendar.getInstance().time.date
            var oldMonth = Calendar.getInstance().time.month
            var oldYear = Calendar.getInstance().time.year

            if (!tvArrivalTime.text.isEmpty() && tvArrivalTime.text.toString() != activityParent.resources.getString(R.string.delivery_choose_time)) {
                val date = tvArrivalTime.text.split(" ")[0]
                val time = tvArrivalTime.text.split(" ")[1]

                oldDate = date.split("-")[0].toInt()
                oldMonth = date.split("-")[1].toInt() - 1
                oldYear = date.split("-")[2].toInt() - 1900

                oldHour = time.split(":")[0].toInt()
                oldMinutes = time.split(":")[1].toInt()
            }

            val datePicker = DatePickerDialog(mContext,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        //minus 1900 because Date() generate date from 1900 (not from 0)
                        //add day 1 because time after choose will less than time generate by Date()
                        var dateChoose = Date((year - 1900), month, dayOfMonth + 1)

                        val timePicker = TimePickerDialog(mContext,
                                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                                    dateChoose = Date((year - 1900), month, dayOfMonth, hourOfDay, (minute + 1))
                                    if (dateChoose < Date()) {
                                        GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.payment_book_date_time_arrival_invalid), false)
                                        tvArrivalTime.text = "${mContext.resources.getString(R.string.delivery_choose_time)}"
                                        tvArrivalTime.clearFocus()
                                    } else {
                                        tvArrivalTime.text = "${String.format("%02d", dayOfMonth)}-${String.format("%02d", (month + 1))}-$year ${String.format("%02d", hourOfDay)}:${String.format("%02d", minute)}"
                                        tvArrivalTime.error = null
                                    }
                                },
                                oldHour,
                                oldMinutes,
                                true)
                        timePicker.show()

                    },
                    oldYear + 1900,
                    oldMonth,
                    oldDate)
            datePicker.datePicker.minDate = Calendar.getInstance().timeInMillis
            datePicker.show()
        }

        layoutNote.setOnClickListener {
            showDialogAddNote()
        }

        layoutCountCustomer.setOnClickListener {
            showDialogChooseCountNumber()
        }
    }

    private fun initFragmentManageOrdering() {
        fragmentManageOrdering = ManageOrderingFragment()
        val bundle = Bundle()
        bundle.putSerializable(Constraint.DATA_SEND, listOrderFood)
        fragmentManageOrdering?.arguments=bundle
        mFragmentManager.beginTransaction().replace(R.id.layoutManageOrdering, fragmentManageOrdering).commitAllowingStateLoss()
    }

    private fun checkValidData(): Boolean {
        var invalidCount = 0

        if (tvCountCustomer.text.isEmpty() || tvCountCustomer.text.toString() == activityParent.resources.getString(R.string.payment_enter_count_customer)) {
            invalidCount++
            showDialogMessage("", activityParent.resources.getString(R.string.payment_book_count_customer_must_enter))
            return false
        }

        if (tvArrivalTime.text.isEmpty() || tvArrivalTime.text.toString() == activityParent.resources.getString(R.string.delivery_choose_time)) {
            invalidCount++
            showDialogMessage("", activityParent.resources.getString(R.string.payment_book_date_time_arrival_must_enter))
            return false
        }

        if (!TextUtils.isDigitsOnly(tvCountCustomer.text)) {
            invalidCount++
            showDialogMessage("", activityParent.resources.getString(R.string.payment_book_count_customer_must_enter))
            return false
        }

        return true
    }

    private fun listOrderIsEmpty(listOrder: ArrayList<DetailOrder>): Boolean {
        listOrder.forEach {
            if (it.numberOrder!! > 0)
                return false
        }
        return true
    }

    private fun showDialogAddNote() {
        val dialogAddNote = DialogAddText(activityParent,
                activityParent.resources.getString(R.string.all_add_note),
                activityParent.resources.getString(R.string.all_add_note),
                activityParent.resources.getString(R.string.all_cancel),
                object : DialogAddText.OnDialogAddTextCallBack {
                    override fun onPositiveClick(value: String) {
                        tvNote.text = value
                    }

                    override fun onNegativeClick() {

                    }
                })
        dialogAddNote.show()
    }

    private fun showDialogChooseCountNumber() {
        var defaultValue = 1
        if (TextUtils.isDigitsOnly(tvCountCustomer.text))
            defaultValue = tvCountCustomer.text.toString().toInt()

        val dialogChooseNumber = DialogChooseNumber(activityParent,
                defaultValue,
                object : DialogChooseNumber.OnNumberSelectedListener {
                    override fun onNumberSelected(value: Int) {
                        tvCountCustomer.text = "$value"
                    }
                })
        dialogChooseNumber.show()
    }


    var onCreateBookCallBack: CreateBookCallBack? = null

    interface CreateBookCallBack {
        fun onCreate(bookNew: CreateBook)
    }

    var onItemBottomNavigationClickListener: OnItemBottomNavigationClickListener? = null

}
