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
import agv.kaak.vn.kaioken.adapter.ViewPagerAdapter
import agv.kaak.vn.kaioken.dialog.DialogDetailCoupon
import agv.kaak.vn.kaioken.dialog.DialogDetailPromotion
import agv.kaak.vn.kaioken.entity.define.UpdateRealtime2Define
import agv.kaak.vn.kaioken.entity.result.NotificationResult
import agv.kaak.vn.kaioken.entity.result.TrackOrder
import agv.kaak.vn.kaioken.fragment.base.BaseSocketFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.utils.BaseSocketEmit
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.util.Log
import android.widget.TextView
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.aurelhubert.ahbottomnavigation.notification.AHNotification
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.fragment_notification.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream

/**
 * A simple [Fragment] subclass.
 *
 */
class NotificationFragment : BaseSocketFragment() {
    companion object {
        val FOR_NOTICE = 1
        val FOR_HISTORY = 2
    }

    private var optionFor = FOR_NOTICE

    private lateinit var fragmentOrdering: ListOrderingFragment
    private lateinit var fragmentNotice: ListNoticeFragment
    private lateinit var fragmentHistory: ListOrderedFragment
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    val baseSocketEmit = BaseSocketEmit()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun getData() {
        //nothing to do
    }

    override fun loadData() {
        baseSocketEmit.listTab(0, 0)
        if (Constraint.callFromNotification) {
            layoutPager.currentItem = 1
            Constraint.callFromNotification = false
        }

    }

    override fun addEvent() {
        //nothing to do
        initViewPager()

        ckbAll.setOnCheckedChangeListener { buttonView, isChecked ->

            when (optionFor) {
                FOR_NOTICE -> {
                    val listNotice = fragmentNotice.getListNotice()
                    listNotice.forEach {
                        it.isChecked = isChecked
                    }
                    fragmentNotice.notifyDataSetChanged()
                }

                FOR_HISTORY -> {
                    val listOrdered = fragmentHistory.getListOrdered()
                    listOrdered.forEach {
                        it.isChecked = isChecked
                    }
                    fragmentHistory.notifyDataSetChanged()
                }
            }

        }

        btnCancel.setOnClickListener {
            when (optionFor) {
                FOR_NOTICE -> {
                    ckbAll.isChecked = false
                    val listNotice = fragmentNotice.getListNotice()
                    listNotice.forEach {
                        it.isChecked = false
                    }
                    fragmentNotice.allowChooseItem(false)
                    layoutOption.visibility = View.GONE
                }

                FOR_HISTORY -> {
                    ckbAll.isChecked = false
                    val listOrdered = fragmentHistory.getListOrdered()
                    listOrdered.forEach {
                        it.isChecked = false
                    }
                    fragmentHistory.allowChooseItem(false)
                    layoutOption.visibility = View.GONE
                }
            }

        }

        btnDelete.setOnClickListener {
            when (optionFor) {
                FOR_NOTICE -> {
                    val itemsSelected: ArrayList<Int> = arrayListOf()
                    val listIndexSelected: ArrayList<Int> = arrayListOf()
                    val listNotice = fragmentNotice.getListNotice()

                    listNotice.forEachIndexed { index, notificationResult ->
                        if (notificationResult.isChecked) {
                            itemsSelected.add(notificationResult.id)
                            listIndexSelected.add(index)
                        }
                    }

                    //can use removeif but API 21 can not call
                    //remove from bottom to top because if remove from to to bottom will change index
                    for (i in listIndexSelected.size - 1 downTo 0)
                        listNotice.removeAt(listIndexSelected[i])

                    fragmentNotice.notifyDataSetChanged()
                    baseSocketEmit.deleteListNotyByUser(itemsSelected, Constraint.uid!!.toInt())

                    //hide layout option
                    ckbAll.isChecked = false
                    fragmentNotice.allowChooseItem(false)
                    layoutOption.visibility = View.GONE
                }

                FOR_HISTORY -> {
                    fragmentHistory.removeListOrder()
                    //hide layout option
                    ckbAll.isChecked = false
                    fragmentHistory.allowChooseItem(false)
                    layoutOption.visibility = View.GONE
                }
            }
        }
    }

    override fun listenSocket() {
        Constraint.BASE_SOCKET?.on("updateRealtime2", onUpdateOrderRealtime)
        Constraint.BASE_SOCKET?.on("listTab", onGetListTitleNotificationListener)
        Constraint.BASE_SOCKET?.on("confirmSeenNoty", onConfirmSeenNotiListener)
        Constraint.BASE_SOCKET?.on("deleteListNotyByUser", onDeleteListNoty)
        Constraint.BASE_SOCKET?.on("noty", onNotyListener)
    }

    override fun offSocket() {
        Constraint.BASE_SOCKET?.off("updateRealtime2")
        Constraint.BASE_SOCKET?.off("listTab")
        Constraint.BASE_SOCKET?.off("confirmSeenNoty")
        Constraint.BASE_SOCKET?.off("deleteListNotyByUser")
    }

    override fun setupComponentStatusSocket(): TextView? {
        return tvStatusSocket
    }

    private fun initListOrderingFragment() {
        fragmentOrdering = ListOrderingFragment()
    }

    private fun initListNoticeFragment() {
        fragmentNotice = ListNoticeFragment()
        fragmentNotice.onItemNoticeClickListener = object : TrackListNoticeAdapter.OnItemNoticeClickListener {
            override fun onSeen(notifyId: Int) {
                baseSocketEmit.confirmSeenNoty(notifyId)
            }

            override fun onNotifiClick(holder: TrackListNoticeAdapter.ViewHolder, notifi: NotificationResult) {
                if (notifi.generalKey != null && notifi.generalKey != -1) {
                    val dialogDetailCoupon = DialogDetailCoupon()
                    val bundle = Bundle()
                    bundle.putInt(Constraint.DATA_SEND, notifi.generalKey!!)
                    bundle.putString(DialogDetailPromotion.NAME_SEND, notifi.info?.pageInfo?.pageName!!)
                    bundle.putString(DialogDetailPromotion.LINK_IMAGE_SEND, notifi.info?.pageInfo?.linkImage!!)
                    dialogDetailCoupon.arguments = bundle
                    dialogDetailCoupon.onClickSeeDetail = object : DialogDetailCoupon.OnClickSeeDetail {
                        override fun onClick() {
                            val intent = Intent(activityParent, InfoStoreActivity::class.java)
                            intent.putExtra(InfoStoreActivity.ID_STORE_SEND, notifi.pageId)
                            intent.putExtra(InfoStoreActivity.NAME_STORE_SEND, notifi.info!!.pageInfo!!.pageName)
                            //Convert to byte array
                            val stream = ByteArrayOutputStream()
                            notifi.imageBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
                            val byteArray = stream.toByteArray()
                            intent.putExtra(InfoStoreActivity.IMAGE_STORE_SEND, byteArray)

                            val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    activityParent,

                                    // Now we provide a list of Pair items which contain the view we can transitioning
                                    // from, and the name of the view it is transitioning to, in the launched activity
                                    Pair<View, String>(holder.tvContent,
                                            InfoStoreActivity.NAME_MAP_TRANSITION),
                                    Pair<View, String>(holder.imgAvatar,
                                            InfoStoreActivity.COVER_MAP_TRANSITION))

                            startActivity(intent, activityOptions.toBundle())
                        }
                    }
                    dialogDetailCoupon.show(mFragmentManager, "DETAIL_COUPON")

                } else {
                    val intent = Intent(activityParent, InfoStoreActivity::class.java)
                    intent.putExtra(InfoStoreActivity.ID_STORE_SEND, notifi.pageId)
                    intent.putExtra(InfoStoreActivity.NAME_STORE_SEND, notifi.info!!.pageInfo!!.pageName)
                    //Convert to byte array
                    val stream = ByteArrayOutputStream()
                    notifi.imageBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    val byteArray = stream.toByteArray()
                    intent.putExtra(InfoStoreActivity.IMAGE_STORE_SEND, byteArray)

                    val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            activityParent,

                            // Now we provide a list of Pair items which contain the view we can transitioning
                            // from, and the name of the view it is transitioning to, in the launched activity
                            Pair<View, String>(holder.tvContent,
                                    InfoStoreActivity.NAME_MAP_TRANSITION),
                            Pair<View, String>(holder.imgAvatar,
                                    InfoStoreActivity.COVER_MAP_TRANSITION))

                    startActivity(intent, activityOptions.toBundle())
                }
            }
        }
        fragmentNotice.onItemLongClickListener = object : OnLongClickListener {
            override fun onLongClick(position: Int, parent: Int) {
                optionFor = parent
                fragmentNotice.allowChooseItem(true)
                layoutOption.visibility = View.VISIBLE
                //ckbAll.isChecked = false
                ViewCompat.setElevation(layoutOption, activityParent.resources.getDimensionPixelOffset(R.dimen.all_masterial_elevation_bottom_nav_bar).toFloat())
            }
        }

        fragmentNotice.onListRefresh = object : ListNoticeFragment.OnListRefresh {
            override fun onRefresh() {
                ckbAll.isChecked = false
            }
        }

        fragmentNotice.onItemCheckedChangeListener = object : OnItemCheckedChangeListener {
            override fun onChange(isChecked: Boolean) {
                //update number selected
                val countItemSelected = countItemNoticeSelected(fragmentNotice.getListNotice())
                btnDelete.text = activityParent.resources.getString(R.string.format_delete_x, countItemSelected)

                if (countItemSelected == 0)
                    btnDelete.visibility = View.GONE
                else
                    btnDelete.visibility = View.VISIBLE
            }
        }
    }

    private fun initListHistoryFragment() {
        fragmentHistory = ListOrderedFragment()

        fragmentHistory.onItemLongClickListener = object : OnLongClickListener {
            override fun onLongClick(position: Int, parent: Int) {
                optionFor = parent
                fragmentHistory.allowChooseItem(true)
                layoutOption.visibility = View.VISIBLE
                //ckbAll.isChecked = false
                ViewCompat.setElevation(layoutOption, activityParent.resources.getDimensionPixelOffset(R.dimen.all_masterial_elevation_bottom_nav_bar).toFloat())
            }
        }

        fragmentHistory.onItemCheckedChangeListener = object : OnItemCheckedChangeListener {
            override fun onChange(isChecked: Boolean) {
                //update number selected
                val countItemSelected = countItemHistorySelected(fragmentHistory.getListOrdered())
                btnDelete.text = activityParent.resources.getString(R.string.format_delete_x, countItemSelected)

                if (countItemSelected == 0)
                    btnDelete.visibility = View.GONE
                else
                    btnDelete.visibility = View.VISIBLE
            }
        }
    }

    private val onUpdateOrderRealtime = Emitter.Listener { args ->
        activityParent.runOnUiThread {
            try {
                Log.d("****updateRealtime2", args[0].toString())
                val jsonResponse = args[0] as JSONObject
                val typeUpdateRealtime = jsonResponse.getInt("type")

                when (typeUpdateRealtime) {

                    UpdateRealtime2Define.CONFIRM_ORDER -> {
                        val orderId = jsonResponse.getJSONObject("data")
                                .getJSONObject("customer")
                                .getInt("order_id")
                        fragmentOrdering.updateStatus(typeUpdateRealtime, orderId, null)
                    }

                    UpdateRealtime2Define.CHEF_START -> {
                        val orderId = jsonResponse.getJSONObject("data")
                                .getJSONObject("all")
                                .getInt("order_id")
                        fragmentOrdering.updateStatus(typeUpdateRealtime, orderId, null)
                    }

                    UpdateRealtime2Define.CHEF_FINISH -> {
                        val orderId = jsonResponse.getJSONObject("data")
                                .getJSONObject("all")
                                .getInt("order_id")
                        fragmentOrdering.updateStatus(typeUpdateRealtime, orderId, null)
                    }

                    UpdateRealtime2Define.REQUIRE_PAYMENT -> {
                        val orderId = jsonResponse.getJSONObject("data")
                                .getJSONObject("customer")
                                .getInt("order_id")
                        fragmentOrdering.updateStatus(typeUpdateRealtime, orderId, null)
                    }

                    UpdateRealtime2Define.ADD_OR_REMOVE_FOOD -> {
                        //data for tab detail order
                        val priceAfter = jsonResponse.getJSONObject("data")
                                .getJSONObject("all")
                                .getJSONObject("order_detail")
                                .getInt("price_after").toDouble()
                        val orderId = jsonResponse.getJSONObject("data")
                                .getJSONObject("all")
                                .getInt("order_id")
                        fragmentOrdering.updateStatus(typeUpdateRealtime, orderId, priceAfter)
                    }

                    UpdateRealtime2Define.ADD_SURCHARGE_OR_DISCOUNT_OR_VAT -> {
                        val orderId = jsonResponse.getJSONObject("data")
                                .getJSONObject("all")
                                .getInt("order_id")
                        val priceAfter = jsonResponse.getJSONObject("data")
                                .getJSONObject("all")
                                .getJSONObject("order_detail")
                                .getDouble("price_after")
                        fragmentOrdering.updateStatus(typeUpdateRealtime, orderId, priceAfter)
                    }

                    UpdateRealtime2Define.CONFIRM_PAYMENT_FROM_CASHIER -> {
                        val orderId = jsonResponse.getJSONObject("data")
                                .getJSONObject("all")
                                .getInt("order_id")
                        fragmentOrdering.updateStatus(typeUpdateRealtime, orderId, null)
                    }

                    UpdateRealtime2Define.CANCEL_ORDER -> {
                        val orderId = jsonResponse.getJSONObject("data")
                                .getJSONObject("customer")
                                .getInt("order_id")
                        fragmentOrdering.updateStatus(typeUpdateRealtime, orderId, null)
                    }

                    UpdateRealtime2Define.SHIPPING -> {
                        val orderId = jsonResponse.getJSONObject("data")
                                .getJSONObject("customer")
                                .getInt("order_id")
                        fragmentOrdering.updateStatus(typeUpdateRealtime, orderId, null)
                    }
                }
            } catch (ex: Exception) {
                //GlobalHelper.showMessage(context, activityParent.resources.getString(R.string.something_went_wrong), true)
                //Log.e("error", ex.message)
            }
        }
    }

    private val onDeleteListNoty = Emitter.Listener {
        activityParent.runOnUiThread {
            Log.d("${Constraint.TAG_LOG}Delete", it[0].toString())
        }
    }

    private val onNotyListener = Emitter.Listener {
        activityParent.runOnUiThread {
            Log.d("${Constraint.TAG_LOG}Noty", it[0].toString())
        }
    }

    private fun initViewPager() {
        //init title

        val itemOrdering = AHBottomNavigationItem(activityParent.resources.getString(R.string.all_order), activityParent.getDrawable(R.drawable.ic_eating))
        layoutTitle.addItem(itemOrdering)

        val itemNotice = AHBottomNavigationItem(activityParent.resources.getString(R.string.all_notice), activityParent.getDrawable(R.drawable.ic_message))
        layoutTitle.addItem(itemNotice)

        val itemHistory = AHBottomNavigationItem(activityParent.resources.getString(R.string.all_history), activityParent.getDrawable(R.drawable.ic_list_task))
        layoutTitle.addItem(itemHistory)

        layoutTitle.defaultBackgroundColor = ContextCompat.getColor(activityParent, R.color.colorMasterialGrey_4)

        layoutTitle.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
        layoutTitle.currentItem = 0

        //init view pager
        viewPagerAdapter = ViewPagerAdapter(mFragmentManager)
        initListOrderingFragment()
        viewPagerAdapter.addFragment(fragmentOrdering, activityParent.resources.getString(R.string.all_order))
        initListNoticeFragment()
        viewPagerAdapter.addFragment(fragmentNotice, activityParent.resources.getString(R.string.all_notice))
        initListHistoryFragment()
        viewPagerAdapter.addFragment(fragmentHistory, activityParent.resources.getString(R.string.all_history))

        layoutPager.adapter = viewPagerAdapter


        layoutTitle.setOnTabSelectedListener { position, _ ->
            layoutPager.setCurrentItem(position, true)
            /*if(position==0)
                fragmentOrdering.refreshList()*/
            return@setOnTabSelectedListener true
        }

        layoutPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                //nothing to do
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                //nothing to do
            }

            override fun onPageSelected(position: Int) {
                layoutTitle.currentItem = position
            }
        })
    }

    var onGetListTitleNotificationListener: Emitter.Listener = Emitter.Listener { args ->
        activityParent.runOnUiThread {
            val jsonResponse = args[0] as JSONObject
            Log.d("****listTab", args[0].toString())
            val countNotice = jsonResponse.getJSONArray("result")
                    .getJSONObject(0)
                    .getInt("count")

            val countNoti = AHNotification.Builder()
                    .setText("$countNotice")
                    .setBackgroundColor(ContextCompat.getColor(activityParent, R.color.colorBluePrimary))
                    .setTextColor(Color.WHITE)
                    .build()
            layoutTitle.setNotification(countNoti, 1)

            fragmentNotice.onRefresh()
        }
    }

    var onConfirmSeenNotiListener: Emitter.Listener = Emitter.Listener { args ->
        activityParent.runOnUiThread {
            Log.d("${Constraint.TAG_LOG}ConfirmSeen", args[0].toString())
            val jsonResponse = args[0] as JSONObject
            val status = jsonResponse.getInt("status")
            //refresh count on title
            if (status == Constraint.STATUS_RIGHT)
                baseSocketEmit.listTab(0, 0)
        }
    }

    fun countItemNoticeSelected(notifications: ArrayList<NotificationResult>): Int {
        var result = 0
        notifications.forEach {
            if (it.isChecked)
                result++
        }
        return result
    }

    fun countItemHistorySelected(history: ArrayList<TrackOrder>): Int {
        var result = 0
        history.forEach {
            if (it.isChecked)
                result++
        }
        return result
    }
}