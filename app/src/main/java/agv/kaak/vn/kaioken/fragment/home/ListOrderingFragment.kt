package agv.kaak.vn.kaioken.fragment.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.activity.InfoStoreActivity
import agv.kaak.vn.kaioken.activity.ProcessActivity
import agv.kaak.vn.kaioken.adapter.TrackListOrderingAdapter
import agv.kaak.vn.kaioken.entity.define.UpdateRealtime2Define
import agv.kaak.vn.kaioken.entity.define.UseType
import agv.kaak.vn.kaioken.entity.result.TrackOrder
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.TrackOrderContract
import agv.kaak.vn.kaioken.mvp.presenter.TrackOrderPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.graphics.Bitmap
import android.provider.Settings
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_list_ordering.*
import java.io.ByteArrayOutputStream

/**
 * A simple [Fragment] subclass.
 *
 */
class ListOrderingFragment : BaseFragment(), TrackOrderContract.View {

    var adapterOrdering: TrackListOrderingAdapter?=null
    val presenter = TrackOrderPresenter(this)
    var listOrdering: ArrayList<TrackOrder> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_ordering, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListOrdering()
        layoutLoading?.visibility = View.VISIBLE
        presenter.getListOrdering()
    }

    override fun getData() {
        //nothing to do
    }

    override fun loadData() {

    }

    override fun addEvent() {
        //nothing to do

    }

    override fun getListOrderingSuccess(listOrder: ArrayList<TrackOrder>) {
        layoutLoading?.visibility = View.GONE

        if (listOrder.isEmpty()) {
            layoutEmpty?.visibility = View.VISIBLE
        } else {
            layoutEmpty?.visibility = View.GONE
            listOrdering.clear()
            listOrdering.addAll(listOrder)
            adapterOrdering?.notifyDataSetChanged()
        }
    }

    override fun getListOrderingFail(msg: String?) {
        layoutLoading?.visibility = View.GONE
        GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.track_order_get_list_ordering_fail), true)
    }

    override fun getListOrderedSuccess(listOrder: ArrayList<TrackOrder>) {
        //nothing to do
    }

    override fun getListOrderedFail(msg: String?) {
        //nothing to do
    }

    override fun removeOrderSuccess() {
        //nothing to do
    }

    override fun removeOrderFail(msg: String?) {
        //nothing to do
    }

    private fun initListOrdering() {
        adapterOrdering = TrackListOrderingAdapter(mContext, listOrdering)
        adapterOrdering?.onButtonClickListener = object : TrackListOrderingAdapter.OnButtonClickListener {
            override fun onSeeDetailStoreClick(holder: TrackListOrderingAdapter.ViewHolder, trackOrder: TrackOrder) {
                Constraint.ID_STORE_ORDERING = trackOrder.pageId!!
                val intent = Intent(activityParent, InfoStoreActivity::class.java)
                intent.putExtra(InfoStoreActivity.ID_STORE_SEND, trackOrder.pageId!!)
                intent.putExtra(InfoStoreActivity.NAME_STORE_SEND, trackOrder.pageName)

                startActivity(intent)
            }

            override fun onSeeDetailOrderClick(storeId: Int, storeName: String, orderId: Int) {
                Constraint.ID_STORE_ORDERING = storeId
                Constraint.NAME_STORE_ORDERING = storeName
                Constraint.ID_ORDERING = orderId
                val intent = Intent(activityParent, ProcessActivity::class.java)
                intent.putExtra(Constraint.ORDER_ID_SEND, orderId)
                startActivity(intent)
            }

            override fun onCallWaiterClick(storeId: Int, orderId: Int) {
                Constraint.ID_STORE_ORDERING = storeId
                GlobalHelper.doCallWaiter(activityParent)
            }

            override fun onCallToStoreClick(phoneNumberStore: String?) {
                GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.all_feature_is_developing), true)
            }

            override fun onSeeSniperClick(orderId: Int) {
                GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.all_feature_is_developing), true)
            }
        }

        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        lstOrdering.adapter = adapterOrdering
        lstOrdering.layoutManager = layoutManager
        lstOrdering.setHasFixedSize(true)
    }

    fun updateStatus(status: Int, orderId: Int, priceAfter: Double?) {
        when (status) {
            UpdateRealtime2Define.CONFIRM_ORDER -> updateStep(1, orderId)
            UpdateRealtime2Define.CHEF_START -> updateStep(2, orderId)
            UpdateRealtime2Define.CHEF_FINISH -> updateStep(3, orderId)
            UpdateRealtime2Define.SHIPPING -> updateStep(3, orderId)
            UpdateRealtime2Define.REQUIRE_PAYMENT -> updateStep(4, orderId)
            UpdateRealtime2Define.ADD_OR_REMOVE_FOOD -> updateSummoney(orderId, priceAfter!!)
            UpdateRealtime2Define.CONFIRM_PAYMENT_FROM_CASHIER -> removeOrder(orderId)
            UpdateRealtime2Define.ADD_SURCHARGE_OR_DISCOUNT_OR_VAT -> updateSummoney(orderId, priceAfter!!)
            UpdateRealtime2Define.CANCEL_ORDER -> removeOrder(orderId)
        }
    }
    private fun updateStep(step: Int, orderId: Int) {
        listOrdering.forEachIndexed { index, trackOrder ->
            if (trackOrder.orderId == orderId) {
                trackOrder.statusId = step
                (lstOrdering.findViewHolderForLayoutPosition(index) as TrackListOrderingAdapter.ViewHolder).updateStep(step)

                //adapterOrdering.notifyItemChanged(index)
            }
        }
    }

    private fun removeOrder(orderId: Int) {
        listOrdering.forEachIndexed { index, trackOrder ->
            if (trackOrder.orderId == orderId) {
                listOrdering.removeAt(index)
                adapterOrdering?.notifyDataSetChanged()
            }
        }

        if (listOrdering.isEmpty())
            layoutEmpty.visibility = View.VISIBLE
        else
            layoutEmpty.visibility = View.GONE
    }

    private fun updateSummoney(orderId: Int, priceAfter: Double) {
        listOrdering.forEachIndexed { index, trackOrder ->
            if (trackOrder.orderId == orderId) {
                trackOrder.priceAfter = priceAfter
                adapterOrdering?.notifyItemChanged(index)
            }
        }
    }

    override fun removeListOrderSuccess() {
        //nothing to do
    }

    override fun removeListOrderFail(msg: String?) {
        //nothing to do
    }
}
