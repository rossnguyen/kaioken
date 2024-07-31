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
import agv.kaak.vn.kaioken.adapter.TrackListNoticeAdapter
import agv.kaak.vn.kaioken.entity.result.NotificationResult
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.NotificationContract
import agv.kaak.vn.kaioken.mvp.presenter.NotificationPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_list_notice.*
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ListNoticeFragment : BaseFragment(), NotificationContract.View, SwipeRefreshLayout.OnRefreshListener {
    private var listNotificationNoticeForCustomerAdapter: TrackListNoticeAdapter? = null
    val presenter = NotificationPresenter(this)
    private var listNotice: ArrayList<NotificationResult> = arrayListOf()

    val LIMIT = 0
    var nothingNotiToLoadMore = false
    var isLoadingMoreNoti = false

    var onItemNoticeClickListener: TrackListNoticeAdapter.OnItemNoticeClickListener? = null
    var onItemLongClickListener: OnLongClickListener? = null
    var onListRefresh: OnListRefresh? = null
    var onItemCheckedChangeListener: OnItemCheckedChangeListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_notice, container, false)
    }


    override fun getData() {
        //nothing to get
    }

    override fun loadData() {
        /*var nothingNotiToLoadMore = false
        var isLoadingMoreNoti = false
        reloadListNotice()*/
    }

    override fun addEvent() {
        initListNotice()
        refreshNotice.setOnRefreshListener(this)
        reloadListNotice()
    }

    override fun getListNoticeNotiSuccess(listNoti: ArrayList<NotificationResult>) {
        /*refreshNotice?.isRefreshing = false
        layoutLoading?.visibility = View.GONE
        if (listNoti.isEmpty())
            layoutEmpty?.visibility = View.VISIBLE
        listNotice.clear()
        listNotice.addAll(listNoti)
        listNotificationNoticeForCustomerAdapter.notifyDataSetChanged()*/
        val oldSize = listNotice.size
        layoutLoading?.visibility = View.GONE
        loadingMoreNoti?.visibility = View.GONE
        refreshNotice?.isRefreshing = false

        if (listNoti.isEmpty() && !isLoadingMoreNoti)
            layoutEmpty?.visibility = View.VISIBLE
        else {
            layoutEmpty?.visibility = View.GONE
            if (!isLoadingMoreNoti)
                listNotice.clear()
            listNotice.addAll(listNoti)
            if (!isLoadingMoreNoti)
                listNotificationNoticeForCustomerAdapter?.notifyDataSetChanged()
            else
                listNotificationNoticeForCustomerAdapter?.notifyItemRangeInserted(oldSize, listNoti.size)

        }

        if (listNoti.size < Constraint.LIMIT)
            nothingNotiToLoadMore = true
        isLoadingMoreNoti = false
    }

    override fun getListNoticeFail(msg: String?) {
        /*refreshNotice?.isRefreshing = false
        layoutLoading?.visibility = View.GONE
        GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.track_order_get_list_notice_fail), true)
        Log.e("****GetListNotice", msg)*/
        layoutLoading?.visibility = View.GONE
        loadingMoreNoti?.visibility = View.GONE
        refreshNotice?.isRefreshing = false
        GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.track_order_get_list_notice_fail), true)
        Log.e("****errorGetListFollow", msg)
        isLoadingMoreNoti = false
        nothingNotiToLoadMore = false
    }

    private fun initListNotice() {
        listNotificationNoticeForCustomerAdapter = TrackListNoticeAdapter(mContext, listNotice)
        if (onItemNoticeClickListener != null)
            listNotificationNoticeForCustomerAdapter?.onItemNoticeClickListener = onItemNoticeClickListener

        val layoutManager = LinearLayoutManager(mContext)
        layoutManager.orientation = LinearLayout.VERTICAL

        lstNotice.adapter = listNotificationNoticeForCustomerAdapter
        lstNotice.layoutManager = layoutManager

        lstNotice?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (!nothingNotiToLoadMore && (layoutManager.findLastCompletelyVisibleItemPosition() == listNotice.size - 1)) {
                    presenter.getListNoticeNoti(Constraint.uid!!.toInt(), 0, 0, listNotice.size, listNotice.size + Constraint.LIMIT)
                    isLoadingMoreNoti = true
                    loadingMoreNoti?.visibility = View.VISIBLE
                }
            }
        })

        listNotificationNoticeForCustomerAdapter?.onItemLongClickListener = onItemLongClickListener
        listNotificationNoticeForCustomerAdapter?.onItemCheckedChangeListener = onItemCheckedChangeListener
    }

    override fun onRefresh() {
        reloadListNotice()
        if (onListRefresh != null)
            onListRefresh!!.onRefresh()
    }

    private fun reloadListNotice() {
        layoutLoading?.visibility = View.VISIBLE
        isLoadingMoreNoti = false
        nothingNotiToLoadMore = false
        presenter.getListNoticeNoti(Constraint.uid!!.toInt(), 0, 0, 0, Constraint.LIMIT)
    }

    fun allowChooseItem(allow: Boolean) {
        listNotificationNoticeForCustomerAdapter?.allowChooseItem(allow)
        listNotificationNoticeForCustomerAdapter?.notifyDataSetChanged()
    }

    fun notifyDataSetChanged() {
        listNotificationNoticeForCustomerAdapter?.notifyDataSetChanged()
    }

    fun getListNotice(): ArrayList<NotificationResult> {
        return listNotice
    }

    interface OnListRefresh {
        fun onRefresh()
    }
}
