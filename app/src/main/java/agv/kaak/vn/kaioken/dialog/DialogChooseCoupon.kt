package agv.kaak.vn.kaioken.dialog

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.ShowListCouponAdapter
import agv.kaak.vn.kaioken.entity.define.DiscountType
import agv.kaak.vn.kaioken.entity.result.Coupon
import agv.kaak.vn.kaioken.entity.result.DetailCoupon
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.CouponContract
import agv.kaak.vn.kaioken.mvp.contract.DetailCouponContract
import agv.kaak.vn.kaioken.mvp.presenter.CouponPresenter
import agv.kaak.vn.kaioken.mvp.presenter.DetailCouponPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.dialog_choose_coupon.*

class DialogChooseCoupon(val context: Activity, val pageId: Int) : Dialog(context),
        CouponContract.View,
        DetailCouponContract.View {

    private lateinit var adapterCoupon: ShowListCouponAdapter

    private val listCoupon: ArrayList<Coupon> = arrayListOf()

    private val detailCouponPresenter = DetailCouponPresenter(this)
    private val couponPresenter = CouponPresenter(this)
    var onCouponChoosedListener: ShowListCouponAdapter.OnCouponChoosedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_choose_coupon)
        addEvent()
        loadData()
    }

    override fun onStart() {
        super.onStart()
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun addEvent() {
        /*groupRadCoupon.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.radEnter) {
                showEnterCoupon()
                hideChooseCoupon()
            } else {
                showChooseCoupon()
                hideEnterCoupon()
            }
        }*/

        radChoose.setOnClickListener {
            radChoose.isChecked = true
            radEnter.isChecked = false
        }

        radEnter.setOnClickListener {
            radEnter.isChecked = true
            radChoose.isChecked = false
        }

        radEnter.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                showEnterCoupon()
                hideChooseCoupon()
            }
        }

        radChoose.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                showChooseCoupon()
                hideEnterCoupon()
            }
        }

        etCoupon.setOnTouchListener(View.OnTouchListener { v, event ->
            val DRAWABLE_RIGHT: Int = 2

            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
                if (event.rawX >= (etCoupon.right - etCoupon.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    doGetCoupon(etCoupon.text.toString())
                }
            }

            return@OnTouchListener false
        })

        etCoupon.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doGetCoupon(etCoupon.text.toString())
                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }
    }

    private fun loadData() {
        initListCoupon()
        layoutLoading.visibility = View.VISIBLE
        couponPresenter.getListCouponOnPage(pageId, Constraint.uid!!.toInt())
    }

    private fun initListCoupon() {
        adapterCoupon = ShowListCouponAdapter(context, listCoupon)

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        lstCoupon.adapter = adapterCoupon
        lstCoupon.layoutManager = layoutManager

        adapterCoupon.onCouponChoosedListener = object : ShowListCouponAdapter.OnCouponChoosedListener {
            override fun onChoosed(type: Int, value: Int, code: String) {
                if (onCouponChoosedListener != null)
                    onCouponChoosedListener!!.onChoosed(type, value, code)
                dismiss()
            }
        }
    }

    override fun getListCouponOnPageSuccess(listCoupon: ArrayList<Coupon>) {
        layoutLoading?.visibility = View.GONE
        this.listCoupon.clear()
        this.listCoupon.add(Coupon(0, DiscountType.VALUE, context.resources.getString(R.string.payment_not_use_coupon)))
        this.listCoupon.addAll(listCoupon.filter { coupon -> coupon.orderId == null })
        if (this.listCoupon.size == 1)
            layoutEmptyCoupon?.visibility = View.VISIBLE
        else {
            adapterCoupon.notifyDataSetChanged()
            lstCoupon?.visibility = View.VISIBLE
        }
    }

    override fun getListCouponOnPageFail(message: String?) {
        layoutLoading?.visibility = View.GONE
        if (message == null || message.isEmpty())
            GlobalHelper.showMessage(context, context.resources.getString(R.string.all_error_global), true)
        else
            GlobalHelper.showMessage(context, message, true)
        layoutEmptyCoupon?.visibility = View.VISIBLE
    }

    override fun getDetailCouponSuccess(detailCoupon: DetailCoupon) {
        if (onCouponChoosedListener != null) {
            onCouponChoosedListener!!.onChoosed(detailCoupon.discountType!!, detailCoupon.discountValue!!, detailCoupon.couponCode!!)
            dismiss()
        }
    }

    override fun getDetailCouponFail(msg: String?) {
        GlobalHelper.showMessage(context, context.getString(R.string.payment_coupon_invalid), false)
    }

    private fun showEnterCoupon() {
        etCoupon.visibility = View.VISIBLE
    }

    private fun hideEnterCoupon() {
        etCoupon.visibility = View.GONE
    }

    private fun showChooseCoupon() {
        layoutChooseCoupon.visibility = View.VISIBLE
    }

    private fun hideChooseCoupon() {
        layoutChooseCoupon.visibility = View.GONE
    }

    private fun doGetCoupon(code: String) {
        //GlobalHelper.showMessage(context,"get coupon",true)
        detailCouponPresenter.getDetailCoupon(Constraint.ID_STORE_ORDERING, code)
    }
}