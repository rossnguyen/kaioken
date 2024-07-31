package agv.kaak.vn.kaioken.fragment.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.activity.InfoStoreActivity
import agv.kaak.vn.kaioken.adapter.CouponAdapter
import agv.kaak.vn.kaioken.entity.result.Coupon
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.CouponContract
import agv.kaak.vn.kaioken.mvp.presenter.CouponPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list_coupon.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ListCouponFragment : BaseFragment(), CouponContract.View {
    val couponPresent = CouponPresenter(this)

    val listCoupon: ArrayList<Coupon> = arrayListOf()
    lateinit var adapterCoupon: CouponAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_coupon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListCouponAdapter()
        layoutLoading.visibility = View.VISIBLE
        couponPresent.getListCouponOnPage(-1, Constraint.uid!!.toInt())
    }

    override fun getData() {
        //nothing to get
    }

    override fun loadData() {
        //nothing to load
    }

    override fun addEvent() {
        ibtnBack.setOnClickListener {
            if (onBackButtonCLickListenter != null)
                onBackButtonCLickListenter!!.onClick(ibtnBack)
        }
    }

    private fun initListCouponAdapter() {
        adapterCoupon = CouponAdapter(mContext, listCoupon)
        adapterCoupon.onCouponClickListener = object : CouponAdapter.OnCouponClickListener {
            override fun onClick(pageId: Int, pageName: String) {
                val intent = Intent(activityParent, InfoStoreActivity::class.java)
                intent.putExtra(InfoStoreActivity.ID_STORE_SEND, pageId)
                intent.putExtra(InfoStoreActivity.NAME_STORE_SEND, pageName)
                startActivity(intent)
            }
        }
        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        lstCoupon.adapter = adapterCoupon
        lstCoupon.layoutManager = layoutManager
    }

    override fun getListCouponOnPageSuccess(listCoupon: ArrayList<Coupon>) {
        layoutLoading.visibility = View.GONE
        this.listCoupon.clear()
        this.listCoupon.addAll(listCoupon)
        adapterCoupon.notifyDataSetChanged()

        if (listCoupon.isEmpty()) {
            layoutEmptyCoupon.visibility = View.VISIBLE
            lstCoupon.visibility = View.GONE
        } else {
            layoutEmptyCoupon.visibility = View.GONE
            lstCoupon.visibility = View.VISIBLE
        }
    }

    override fun getListCouponOnPageFail(message: String?) {
        layoutLoading.visibility = View.GONE
        if (message == null || message.isEmpty())
            GlobalHelper.showMessage(mContext, mContext.resources.getString(R.string.all_error_global), true)
        else
            GlobalHelper.showMessage(mContext, message, true)
    }

    var onBackButtonCLickListenter: View.OnClickListener? = null
}
