package agv.kaak.vn.kaioken.fragment.order


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.activity.ChooseCoordinatesActivity
import agv.kaak.vn.kaioken.adapter.ConfirmFoodAdapter
import agv.kaak.vn.kaioken.adapter.ShowListCouponAdapter
import agv.kaak.vn.kaioken.adapter.ShowMenuItemAdapter
import agv.kaak.vn.kaioken.dialog.DialogAddText
import agv.kaak.vn.kaioken.dialog.DialogChooseCoupon
import agv.kaak.vn.kaioken.entity.CreateDelivery
import agv.kaak.vn.kaioken.entity.DetailOrder
import agv.kaak.vn.kaioken.entity.define.BottomNavigationCode
import agv.kaak.vn.kaioken.entity.define.DiscountType
import agv.kaak.vn.kaioken.entity.define.PaymentType
import agv.kaak.vn.kaioken.entity.result.DetailCoupon
import agv.kaak.vn.kaioken.entity.result.KiOfPageResult
import agv.kaak.vn.kaioken.entity.result.KiOfUserResult
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.helper.MapUtils
import agv.kaak.vn.kaioken.mvp.contract.DetailCouponContract
import agv.kaak.vn.kaioken.mvp.contract.KiContract
import agv.kaak.vn.kaioken.mvp.presenter.DetailCouponPresenter
import agv.kaak.vn.kaioken.mvp.presenter.KiPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import agv.kaak.vn.kaioken.utils.implement.OnItemBottomNavigationClickListener
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_info_delivery.*
import kotlinx.android.synthetic.main.layout_delivery_info.*
import kotlinx.android.synthetic.main.layout_order_info.*
import java.util.*

class InfoDeliveryFragment : BaseFragment(), KiContract.View {
    companion object {
        const val REQUEST_ADDRESS_CODE = 1212
    }

    private var fragmentManageOrdering: ManageOrderingFragment? = null
    private lateinit var listOrderFood: ArrayList<DetailOrder>
    private var discountCode = ""
    private var coordinates = ""
    var address = ""

    var finalPrice = 0.toDouble()
    var paymentType = PaymentType.CASH

    val kiOfUserPresenter = KiPresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_delivery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDefaultAddressAndCoordinates()
        initFragmentManageOrdering()
    }

    override fun getData() {
        listOrderFood = arguments!!.getSerializable(OrderFragment.LIST_ORDER_FOOD_SEND) as ArrayList<DetailOrder>
    }

    override fun loadData() {}

    override fun addEvent() {
        btnConfirm.setOnClickListener {
            if (checkValidData()) {
                if (listOrderIsEmpty(listOrderFood)) {
                    showDialogMessage(mContext.resources.getString(R.string.order_item_is_null), null)
                    return@setOnClickListener
                }

                if (onItemBottomNavigationClickListener != null)
                    onItemBottomNavigationClickListener!!.onClick(BottomNavigationCode.CONFIRM_DELIVERY)
                if (onCreateDeliveryCallBack != null) {
                    val canWaitTo = ConvertHelper.stringToDateTime("${tvCanWaitTo.text}:00")

                    val deliveryNew = CreateDelivery(Constraint.uid!!,
                            Constraint.phoneUser,
                            address,
                            coordinates,
                            canWaitTo,
                            discountCode,
                            tvNote.text.toString(),
                            OrderFragment.TYPE_ORDER_DELIVERY,
                            Constraint.ID_STORE_ORDERING.toString(),
                            paymentType.toString(),
                            "hello",
                            DiscountType.VALUE.toString(),
                            "0",
                            "abc@gmail.com",
                            listOrderFood)
                    onCreateDeliveryCallBack!!.onCreate(deliveryNew)
                }
            }
        }

        layoutSchedule.setOnClickListener {
            var oldHour = 0
            var oldMinutes = 0
            var oldDate = Calendar.getInstance().time.date
            var oldMonth = Calendar.getInstance().time.month
            var oldYear = Calendar.getInstance().time.year

            if (!tvCanWaitTo.text.isEmpty()) {
                val date = tvCanWaitTo.text.split(" ")[0]
                val time = tvCanWaitTo.text.split(" ")[1]

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
                                        GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.payment_delivery_date_time_arrival_invalid), false)
                                        tvCanWaitTo.text = ""
                                        tvCanWaitTo.clearFocus()
                                    } else {
                                        tvCanWaitTo.text = "${String.format("%02d", dayOfMonth)}-${String.format("%02d", (month + 1))}-$year ${String.format("%02d", hourOfDay)}:${String.format("%02d", minute)}"
                                        tvCanWaitTo.error = null
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

        layoutCoupon.setOnClickListener {
            showDialogChooseCoupon()
        }

        layoutDeliveryAddress.setOnClickListener {
            if (GlobalHelper.gpsIsOn(activityParent))
                chooseAddress(tvDeliveryAddress.text.toString())
        }

        layoutNote.setOnClickListener {
            showDialogAddNote()
        }

        btnPaymentTypeCash.setOnClickListener {
            highlightPaymentTypeCash()
            paymentType = PaymentType.CASH
        }

        btnPaymentTypeKi.setOnClickListener {
            highlightPaymentTypeKi()
            paymentType = PaymentType.KI
        }
    }

    private fun initFragmentManageOrdering() {
        fragmentManageOrdering = ManageOrderingFragment()
        fragmentManageOrdering?.onFinalMoneyChangeListener = object : ManageOrderingFragment.OnFinalMoneyChangeListener {
            override fun onChange(value: Double) {
                setTextForPaymentType(value)
            }
        }
        val bundle = Bundle()
        bundle.putSerializable(Constraint.DATA_SEND, listOrderFood)
        fragmentManageOrdering?.arguments = bundle

        mFragmentManager.beginTransaction().replace(R.id.layoutManageOrdering, fragmentManageOrdering).commitAllowingStateLoss()
    }

    private fun showDialogChooseCoupon() {
        val dialogChooseCoupon = DialogChooseCoupon(activityParent, Constraint.ID_STORE_ORDERING)
        dialogChooseCoupon.onCouponChoosedListener = object : ShowListCouponAdapter.OnCouponChoosedListener {
            override fun onChoosed(type: Int, value: Int, code: String) {
                if (value > 0) {
                    if (type == DiscountType.PERCENT)
                        tvCouponCode?.text = activityParent.resources.getString(R.string.payment_use_coupon_x, activityParent.resources.getString(R.string.format_x_percent, value))
                    else
                        tvCouponCode?.text = activityParent.resources.getString(R.string.payment_use_coupon_x, activityParent.resources.getString(R.string.format_x_k, value))

                    fragmentManageOrdering?.applyCoupon(type, value)
                    discountCode = code
                } else {
                    discountCode = ""
                    tvCouponCode?.text = activityParent.resources.getString(R.string.payment_enter_coupon)
                    fragmentManageOrdering?.applyCoupon(DiscountType.VALUE, 0)
                }
            }
        }
        dialogChooseCoupon.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ADDRESS_CODE && resultCode == Activity.RESULT_OK) {
            address = data!!.getStringExtra(ChooseCoordinatesActivity.ADDRESS_CALL_BACK)
            coordinates = data.getStringExtra(ChooseCoordinatesActivity.COORDINATES_CALL_BACK)
            updateAddressDelivery()
        }
    }

    private fun setDefaultAddressAndCoordinates() {
        if (Constraint.myLocation == null) {
            tvDeliveryAddress.text = activityParent.resources.getString(R.string.delivery_please_choose_address)
        } else {
            coordinates = "${Constraint.myLocation?.latitude}, ${Constraint.myLocation?.longitude}"
            address = MapUtils.getAddressFromCoordinates(mContext, Constraint.myLocation)
            updateAddressDelivery()
        }

    }

    private fun checkValidData(): Boolean {
        var invalidCount = 0
        if (tvDeliveryAddress.text == activityParent.resources.getString(R.string.delivery_please_choose_address) || address == "") {
            showDialogMessage("", activityParent.resources.getString(R.string.delivery_please_choose_address))
            return false
        }

        if (tvCanWaitTo.text.isEmpty()) {
            showDialogMessage("", activityParent.resources.getString(R.string.payment_delivery_time_arrival_must_enter))
            return false
        }

        if (invalidCount == 0)
            return true

        return false
    }

    private fun listOrderIsEmpty(listOrder: ArrayList<DetailOrder>): Boolean {
        listOrder.forEach {
            if (it.numberOrder!! > 0)
                return false
        }
        return true
    }

    private fun chooseAddress(defaultAddress: String) {
        val intentChooseAddress = Intent(activityParent, ChooseCoordinatesActivity::class.java)
        intentChooseAddress.putExtra(Constraint.DATA_SEND, defaultAddress)
        startActivityForResult(intentChooseAddress, REQUEST_ADDRESS_CODE)
    }

    private fun updateAddressDelivery() {
        tvDeliveryAddress.text = address
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

    private fun highlightPaymentTypeCash() {
        btnPaymentTypeCash.setBackgroundResource(R.drawable.custom_background_short_corner_material_4_gray_border)
        btnPaymentTypeKi.setBackgroundResource(0)

    }

    private fun highlightPaymentTypeKi() {
        btnPaymentTypeCash.setBackgroundResource(0)
        btnPaymentTypeKi.setBackgroundResource(R.drawable.custom_background_short_corner_material_4_gray_border)
    }

    private fun setTextForPaymentType(value: Double) {
        tvCash?.text = ConvertHelper.doubleToMoney(mContext, value)
        tvKi?.text=ConvertHelper.doubleToMoney(mContext,value)
    }

    override fun getListKiOfUserSuccess(listKiOfUser: KiOfUserResult) {
        //do nothing
    }

    override fun getListKiOfUserFail(msg: String?) {
        //do nothing
    }

    override fun getKiOfPageSuccess(kiOfPage: KiOfPageResult) {
        layoutLoading.visibility = View.GONE
        if (kiOfPage.kiPoint!! >= finalPrice)
            btnPaymentTypeKi.visibility = View.VISIBLE
        else
            btnPaymentTypeKi.visibility = View.GONE
    }

    override fun getKiOfPageFail(msg: String?) {
        layoutLoading.visibility = View.GONE
    }

    var onCreateDeliveryCallBack: CreateDeliveryCallBack? = null

    interface CreateDeliveryCallBack {
        fun onCreate(deliveryNew: CreateDelivery)
    }

    var onItemBottomNavigationClickListener: OnItemBottomNavigationClickListener? = null
}
