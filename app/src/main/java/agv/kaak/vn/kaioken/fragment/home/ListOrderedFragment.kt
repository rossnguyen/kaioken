package agv.kaak.vn.kaioken.fragment.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken._interface.OnItemCheckedChangeListener
import agv.kaak.vn.kaioken._interface.OnLongClickListener
import agv.kaak.vn.kaioken.activity.InfoStoreActivity
import agv.kaak.vn.kaioken.activity.ProcessActivity
import agv.kaak.vn.kaioken.activity.SeeInvoiceDetailActivity
import agv.kaak.vn.kaioken.adapter.TrackListOrderedAdapter
import agv.kaak.vn.kaioken.adapter.TrackListOrderingAdapter
import agv.kaak.vn.kaioken.dialog.DialogRatingAfterPayment
import agv.kaak.vn.kaioken.entity.result.TrackOrder
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.TrackOrderContract
import agv.kaak.vn.kaioken.mvp.presenter.TrackOrderPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import kotlinx.android.synthetic.main.fragment_list_ordered.*
import java.io.ByteArrayOutputStream

class ListOrderedFragment : BaseFragment(), TrackOrderContract.View {
    private lateinit var adapterOrdered: TrackListOrderedAdapter
    val presenter = TrackOrderPresenter(this)

    private var listOrdered: ArrayList<TrackOrder> = arrayListOf()
    var listIndexSelected: ArrayList<Int> = arrayListOf()
    var templeIndex = -1
    var nothingToLoadMore = false
    var isLoadMore = false

    var onItemLongClickListener: OnLongClickListener? = null
    var onItemCheckedChangeListener: OnItemCheckedChangeListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_ordered, container, false)
    }

    override fun getData() {
        //nothing to get
    }

    override fun loadData() {
        nothingToLoadMore = false
    }

    override fun addEvent() {
        initListOrdered()
        layoutLoading.visibility = View.VISIBLE
        presenter.getListOrdered(0, Constraint.LIMIT)
    }

    override fun getListOrderingSuccess(listOrder: ArrayList<TrackOrder>) {
        //nothing to do
    }

    override fun getListOrderingFail(msg: String?) {
        //nothing to do
    }

    override fun removeOrderSuccess() {
        listOrdered.removeAt(templeIndex)
        adapterOrdered.notifyItemRemoved(templeIndex)
        templeIndex = -1
    }

    override fun removeOrderFail(msg: String?) {
        if (msg == null || msg.isEmpty())
            GlobalHelper.showMessage(mContext, mContext.resources.getString(R.string.all_error_global), true)
        else
            GlobalHelper.showMessage(mContext, msg, true)
        templeIndex = -1
    }

    override fun getListOrderedSuccess(listOrder: ArrayList<TrackOrder>) {
        layoutLoading?.visibility = View.GONE
        val oldSize = listOrdered.size

        if (listOrder.isEmpty() && !isLoadMore) {
            layoutEmpty?.visibility = View.VISIBLE
        } else {
            layoutEmpty?.visibility = View.GONE
            if (!isLoadMore)
                listOrdered.clear()
            listOrdered.addAll(listOrder)

            if (!isLoadMore)
                adapterOrdered.notifyDataSetChanged()
            else
                adapterOrdered.notifyItemRangeInserted(oldSize, listOrder.size)

        }

        if (listOrder.size < Constraint.LIMIT)
            nothingToLoadMore = true
        isLoadMore = false
        loadingMore?.visibility = View.GONE
    }

    override fun getListOrderedFail(msg: String?) {
        layoutLoading?.visibility = View.GONE
        if (msg == null || msg.isEmpty())
            GlobalHelper.showMessage(mContext, mContext.resources.getString(R.string.all_error_global), true)
        else
            GlobalHelper.showMessage(mContext, msg, true)
        isLoadMore = false
        loadingMore?.visibility = View.GONE
    }

    private fun initListOrdered() {
        adapterOrdered = TrackListOrderedAdapter(mContext, listOrdered)
        adapterOrdered.onButtonClick = object : TrackListOrderedAdapter.OnItemClickListener {

            override fun onClickDetailStore(holder: TrackListOrderedAdapter.ViewHolder, trackOrder: TrackOrder) {
                Constraint.ID_STORE_ORDERING = trackOrder.pageId!!

                val intent = Intent(activityParent, InfoStoreActivity::class.java)
                intent.putExtra(InfoStoreActivity.ID_STORE_SEND, trackOrder.pageId!!)
                intent.putExtra(InfoStoreActivity.NAME_STORE_SEND, trackOrder.pageName)
                //Convert to byte array
                val stream = ByteArrayOutputStream()
                trackOrder.imageBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, stream);
                val byteArray = stream.toByteArray()
                intent.putExtra(InfoStoreActivity.IMAGE_STORE_SEND, byteArray)

                val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activityParent,

                        // Now we provide a list of Pair items which contain the view we can transitioning
                        // from, and the name of the view it is transitioning to, in the launched activity
                        Pair<View, String>(holder.tvNameStore,
                                InfoStoreActivity.NAME_MAP_TRANSITION),
                        Pair<View, String>(holder.imgAvatarStore,
                                InfoStoreActivity.COVER_MAP_TRANSITION))

                startActivity(intent, activityOptions.toBundle())
            }

            override fun onClickRate(storeId: Int?, storeName: String?, orderId: Int?, index: Int) {
                templeIndex = index
                val dialogRate = DialogRatingAfterPayment(activityParent, storeId, storeName, orderId)
                dialogRate.show()
            }

            override fun onClickDelete(orderId: Int, index: Int) {
                templeIndex = index
                presenter.removeOrdered(orderId)
            }

            override fun onClickInvoiceDetail(invoiceNo: String?) {
                val intentInvoiceDetail = Intent(activityParent, SeeInvoiceDetailActivity::class.java)
                intentInvoiceDetail.putExtra(Constraint.DATA_SEND, invoiceNo)
                startActivity(intentInvoiceDetail)
            }
        }

        adapterOrdered.onItemLongClickListener = onItemLongClickListener
        adapterOrdered.onItemCheckedChangeListener = onItemCheckedChangeListener

        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        lstOrdered.adapter = adapterOrdered
        lstOrdered.layoutManager = layoutManager

        lstOrdered.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (!nothingToLoadMore && (layoutManager.findLastCompletelyVisibleItemPosition() == listOrdered.size - 1)) {
                    presenter.getListOrdered(listOrdered.size, listOrdered.size + Constraint.LIMIT)
                    isLoadMore = true
                    loadingMore?.visibility = View.VISIBLE
                }
            }
        })
    }

    fun allowChooseItem(allow: Boolean) {
        adapterOrdered.allowChooseItem(allow)
        adapterOrdered.notifyDataSetChanged()
    }

    fun notifyDataSetChanged() {
        adapterOrdered.notifyDataSetChanged()
    }

    fun getListOrdered(): ArrayList<TrackOrder> {
        return listOrdered
    }

    fun removeListOrder() {
        val itemsSelected: ArrayList<Int> = arrayListOf()

        listOrdered.forEachIndexed { index, trackOrder ->
            if (trackOrder.isChecked) {
                itemsSelected.add(trackOrder.orderId!!)
                listIndexSelected.add(index)
            }
        }

        presenter.removeListOrdered(itemsSelected)
    }

    override fun removeListOrderSuccess() {
        //can use removeif but API 21 can not call
        //remove from bottom to top because if remove from to to bottom will change index
        for (i in listIndexSelected.size - 1 downTo 0)
            listOrdered.removeAt(listIndexSelected[i])
        notifyDataSetChanged()

        GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.all_deleted_successfully), true)
    }

    override fun removeListOrderFail(msg: String?) {
        GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.all_delete_fail), true)
    }
}
