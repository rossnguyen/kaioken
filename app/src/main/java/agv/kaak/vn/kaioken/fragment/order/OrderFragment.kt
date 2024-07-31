package agv.kaak.vn.kaioken.fragment.order


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.activity.HomeActivity
import agv.kaak.vn.kaioken.activity.InfoStoreActivity
import agv.kaak.vn.kaioken.activity.ProcessActivity
import agv.kaak.vn.kaioken.adapter.ShowMenuItemAdapter
import agv.kaak.vn.kaioken.entity.*
import agv.kaak.vn.kaioken.entity.define.BottomNavigationCode
import agv.kaak.vn.kaioken.entity.define.DiscountType
import agv.kaak.vn.kaioken.entity.define.UseType
import agv.kaak.vn.kaioken.fragment.base.BaseSocketFragment
import agv.kaak.vn.kaioken.fragment.home.ScanQRFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.utils.BaseSocketEmit
import agv.kaak.vn.kaioken.utils.Constraint
import agv.kaak.vn.kaioken.utils.implement.OnItemBottomNavigationClickListener
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.RelativeLayout
import android.widget.TextView
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.fragment_order.*
import org.json.JSONObject

class OrderFragment : BaseSocketFragment(), OnItemBottomNavigationClickListener {
    companion object {
        const val LIST_ORDER_FOOD_SEND = "LIST_ORDER_FOOD_SEND"
        const val LIST_ORDER_ROOM_SEND = "LIST_ORDER_ROOM_SEND"
        const val DATA_STORE_SEND = "DATA_STORE_SEND"

        const val TYPE_ORDER_LOCAL = 3
        const val TYPE_ORDER_BOOK = 2
        const val TYPE_ORDER_DELIVERY = 1

        var isApplyPromotionAllOrder = false
        var storePromotion: StorePromotion? = null
    }

    val FRAGMENT_CHOOSE = 0
    val FRAGMENT_CONFIRM = 1
    val FRAGMENT_SEARCH = 2
    val FRAGMENT_INFO_BOOK = 3
    val FRAGMENT_INFO_DELIVERY = 4

    var tagFragmentIsShowing = 0

    private var chooseOrderFragment: ChooseOrderFragment? = null
    private var confirmOrderFragment: ConfirmOrderFragment? = null
    private var searchFoodFragment: SearchFoodFragment? = null
    private var infoBookFragment: InfoBookFragment? = null
    private var infoDeliveryFragment: InfoDeliveryFragment? = null

    private lateinit var progressDialog: ProgressDialog
    private val baseSocketEmit = BaseSocketEmit()

    private var listOrderFood: ArrayList<DetailOrder> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun getData() {
        //nothing to do
    }

    override fun loadData() {
        setUpActionBar()
        setFragmentIsShowing(tagFragmentIsShowing)
    }

    override fun addEvent() {
        ibtnBack.setOnClickListener {
            when (tagFragmentIsShowing) {
                FRAGMENT_CONFIRM -> setFragmentIsShowing(FRAGMENT_CHOOSE)
                FRAGMENT_SEARCH -> setFragmentIsShowing(FRAGMENT_CHOOSE)
                FRAGMENT_CHOOSE -> activityParent.onBackPressed()
                FRAGMENT_INFO_BOOK -> setFragmentIsShowing(FRAGMENT_CHOOSE)
                FRAGMENT_INFO_DELIVERY -> setFragmentIsShowing(FRAGMENT_CHOOSE)
            }
        }

        svSearchFood.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                tvName.visibility = View.GONE
                val paramsSearch = svSearchFood.layoutParams as RelativeLayout.LayoutParams
                paramsSearch.addRule(RelativeLayout.END_OF, R.id.ibtnBack)
                paramsSearch.width = RelativeLayout.LayoutParams.MATCH_PARENT
                svSearchFood.layoutParams = paramsSearch
                //show fragment result search
                setFragmentIsShowing(FRAGMENT_SEARCH)
            } else {
                tvName.visibility = View.VISIBLE
                val paramsSearch = svSearchFood.layoutParams as RelativeLayout.LayoutParams
                paramsSearch.removeRule(RelativeLayout.END_OF)
                paramsSearch.width = RelativeLayout.LayoutParams.WRAP_CONTENT
                svSearchFood.layoutParams = paramsSearch
                //hide show fragment choose foodWithTopping
                setFragmentIsShowing(FRAGMENT_CHOOSE)
            }
        }

        svSearchFood.setOnQueryTextListener(object : android.support.v7.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchFoodFragment?.searchFood(newText)
                return false
            }
        })

        ibtnCallWaiter.setOnClickListener {
            GlobalHelper.doCallWaiter(mContext)
        }

    }

    override fun listenSocket() {
        Constraint.BASE_SOCKET?.on("createOrderS1", onCreateOrderListener)
        Constraint.BASE_SOCKET?.on("extraFoodItemList", onAddFoodToOrderListener)
    }

    override fun offSocket() {
        Constraint.BASE_SOCKET?.off("createOrderS1")
        Constraint.BASE_SOCKET?.off("extraFoodItemList")
    }

    override fun setupComponentStatusSocket(): TextView? {
        return tvStatusSocket
    }

    fun doCreateOrder(orderNew: CreateOrder) {
        //remove order have number order is 0
        orderNew.listOrder.forEach {
            if (it.numberOrder == 0)
                orderNew.listOrder.remove(it)
        }
        //check status socket before emit
        if (!GlobalHelper.networkIsConnected(activityParent))
            return
        //openActivityProcess();
        //create new order

        Log.d("****createOrderLocal", orderNew.toJsonObject().toString())

        progressDialog = GlobalHelper.createProgressDialogHandling(activityParent, R.style.progressDialogPrimary)
        progressDialog.show()

        if (socketReady) {
            baseSocketEmit.createOrder(orderNew)
            Constraint.BASE_SOCKET?.on(Socket.EVENT_CONNECT) {
                baseSocketEmit.createOrder(orderNew)
            }
        } else
            Handler().postDelayed({
                baseSocketEmit.createOrder(orderNew)
                Constraint.BASE_SOCKET?.on(Socket.EVENT_CONNECT) {
                    baseSocketEmit.createOrder(orderNew)
                }
            }, Constraint.delayBeforeOnSocket)
    }

    fun doAddFood(addFoodToOrder: AddFoodToOrder) {
        addFoodToOrder.listItem.forEach {
            if (it.numberOrder == 0)
                addFoodToOrder.listItem.remove(it)
        }
        //check status socket before emit
        if (!GlobalHelper.networkIsConnected(activityParent))
            return
        //openActivityProcess();
        //create new order

        Log.d("****AddFoodToOrder", addFoodToOrder.toJsonObject().toString())

        progressDialog = GlobalHelper.createProgressDialogHandling(activityParent, R.style.progressDialogPrimary)
        progressDialog.show()

        if (socketReady) {
            baseSocketEmit.extraFoodItemList(addFoodToOrder)
            Constraint.BASE_SOCKET?.on(Socket.EVENT_CONNECT) {
                baseSocketEmit.extraFoodItemList(addFoodToOrder)
            }
        } else
            Handler().postDelayed({
                baseSocketEmit.extraFoodItemList(addFoodToOrder)
                Constraint.BASE_SOCKET?.on(Socket.EVENT_CONNECT) {
                    baseSocketEmit.extraFoodItemList(addFoodToOrder)
                }
            }, Constraint.delayBeforeOnSocket)
    }

    fun doCreateDelivery(deliveryNew: CreateDelivery) {
        //remove order have number order is 0
        deliveryNew.listOrder!!.forEach {
            if (it.numberOrder == 0)
                deliveryNew.listOrder!!.remove(it)
        }
        //check status socket before emit
        if (!GlobalHelper.networkIsConnected(activityParent))
            return
        //create new order

        Log.d("****createDelivery", deliveryNew.toJsonObject().toString())
        progressDialog = GlobalHelper.createProgressDialogHandling(activityParent, R.style.progressDialogPrimary)
        progressDialog.show()

        if (socketReady) {
            baseSocketEmit.createDelivery(deliveryNew)
            Constraint.BASE_SOCKET?.on(Socket.EVENT_CONNECT) {
                baseSocketEmit.createDelivery(deliveryNew)
            }
        } else
            Handler().postDelayed({
                baseSocketEmit.createDelivery(deliveryNew)
                Constraint.BASE_SOCKET?.on(Socket.EVENT_CONNECT) {
                    baseSocketEmit.createDelivery(deliveryNew)
                }
            }, Constraint.delayBeforeOnSocket)
    }

    fun doCreateBook(bookNew: CreateBook) {
        //remove order have number order is 0
        bookNew.listOrder!!.forEach {
            if (it.numberOrder == 0)
                bookNew.listOrder!!.remove(it)
        }
        //check status socket before emit
        if (!GlobalHelper.networkIsConnected(activityParent))
            return
        //create new order

        Log.d("****createBook", bookNew.toJsonObject().toString())
        //GlobalHelper.showMessage(mContext,"Just click create book", true)
        progressDialog = GlobalHelper.createProgressDialogHandling(activityParent, R.style.progressDialogPrimary)
        progressDialog.show()

        if (socketReady) {
            baseSocketEmit.createBook(bookNew)
            Constraint.BASE_SOCKET?.on(Socket.EVENT_CONNECT) {
                baseSocketEmit.createBook(bookNew)
            }
        } else
            Handler().postDelayed({
                baseSocketEmit.createBook(bookNew)
                Constraint.BASE_SOCKET?.on(Socket.EVENT_CONNECT) {
                    baseSocketEmit.createBook(bookNew)
                }
            }, Constraint.delayBeforeOnSocket)
    }

    private val onCreateOrderListener = Emitter.Listener { args ->
        activityParent.runOnUiThread {
            Constraint.BASE_SOCKET?.off(Socket.EVENT_CONNECT)
            progressDialog.dismiss()
            try {
                Log.d("****CreateOrder", args[0].toString())
                val jsonResult = args[0] as JSONObject
                val status = jsonResult.getInt("status")
                if (status == 1) {
                    val orderId = jsonResult.getJSONObject("result")
                            .getInt("order_id")
                    val message = jsonResult.getJSONObject("result")
                            .getString("message")
                    GlobalHelper.showMessage(activityParent,
                            message,
                            true)
                    //move to activity process
                    Constraint.ID_ORDERING = orderId
                    val intent = Intent(activityParent, ProcessActivity::class.java)
                    intent.putExtra(Constraint.ORDER_ID_SEND, orderId)
                    startActivity(intent)
                } else
                    showDialogMessage(null, resources.getString(R.string.choose_food_create_order_fail))

            } catch (ex: Exception) {
                Log.d("****error", "Lỗi lúc nhận dữ liệu trả về: " + args[0].toString())
            }
        }
    }

    private val onAddFoodToOrderListener = Emitter.Listener { args ->
        activityParent.runOnUiThread {
            Constraint.BASE_SOCKET?.off(Socket.EVENT_CONNECT)
            progressDialog.dismiss()
            try {
                Log.d("****AddFoodToOrder", args[0].toString())
                val jsonResult = args[0] as JSONObject
                val status = jsonResult.getInt("status")
                val orderId = jsonResult.getJSONObject("result")
                        .getInt("order_id")
                val message = jsonResult.getJSONObject("result")
                        .getString("message")
                if (status == 1) {
                    GlobalHelper.showMessage(activityParent,
                            message,
                            true)
                    //move to activity process
                    Constraint.ID_ORDERING = orderId
                    val intent = Intent(activityParent, ProcessActivity::class.java)
                    intent.putExtra(Constraint.ORDER_ID_SEND, orderId)
                    startActivity(intent)
                    Constraint.IS_ADD_FOOD = false
                } else
                    showDialogMessage(null, resources.getString(R.string.choose_food_create_order_fail))


            } catch (ex: Exception) {
                Log.d("****error", "Lỗi lúc nhận dữ liệu trả về: " + args[0].toString())
            }
        }
    }

    override fun onClick(code: Int) {
        when (code) {
            BottomNavigationCode.GO_TO_CONFIRM -> {
                setFragmentIsShowing(FRAGMENT_CONFIRM)
                /*when (Constraint.TYPE_USE) {
                    UseType.LOCAL_HAVE_TABLE -> setFragmentIsShowing(FRAGMENT_CONFIRM)
                    *//*UseType.BOOK -> setFragmentIsShowing(FRAGMENT_INFO_BOOK)
                    UseType.DELIVERY -> setFragmentIsShowing(FRAGMENT_INFO_DELIVERY)*//*
                }*/
            }
            BottomNavigationCode.SEND_ORDER_LOCAL -> {

            }
            BottomNavigationCode.CONFIRM_BOOK -> {
                setFragmentIsShowing(FRAGMENT_INFO_BOOK)
            }

            BottomNavigationCode.CONFIRM_DELIVERY -> {
                setFragmentIsShowing(FRAGMENT_INFO_DELIVERY)
            }
            //BottomNavigationCode.SEND_ORDER_LOCAL->startActivity(Intent(activityParent,ProcessActivity::class.java))
        }
    }

    fun setFragmentIsShowing(tag: Int) {
        val fragmentTransaction = mFragmentManager.beginTransaction()
        layoutToolbar.visibility = View.VISIBLE
        when (tag) {
            FRAGMENT_CHOOSE -> {
                svSearchFood.setQuery("", false)
                svSearchFood.clearFocus()
                svSearchFood.setIconified(true)

                if (chooseOrderFragment == null)
                    initChooseOrderFragment()

                fragmentTransaction.replace(R.id.layoutContent, chooseOrderFragment)
                        .commitAllowingStateLoss()

                tagFragmentIsShowing = FRAGMENT_CHOOSE

                svSearchFood.visibility = View.VISIBLE
                ibtnBack.visibility = View.VISIBLE
                tvName.visibility = View.VISIBLE
            }

            FRAGMENT_CONFIRM -> {
                if (confirmOrderFragment == null)
                    initConfirmOrderFragment()

                fragmentTransaction.replace(R.id.layoutContent, confirmOrderFragment)
                        .commitAllowingStateLoss()

                tagFragmentIsShowing = FRAGMENT_CONFIRM

                svSearchFood.visibility = View.GONE
                ibtnBack.visibility = View.VISIBLE
                tvName.visibility = View.VISIBLE
            }

            FRAGMENT_SEARCH -> {
                initSearchFragment()

                fragmentTransaction.replace(R.id.layoutContent, searchFoodFragment)
                        .commitAllowingStateLoss()

                tagFragmentIsShowing = FRAGMENT_SEARCH

                ibtnBack.visibility = View.VISIBLE
                tvName.visibility = View.GONE
            }

            FRAGMENT_INFO_BOOK -> {
                if (infoBookFragment == null)
                    initInfoBookFragment()
                fragmentTransaction.replace(R.id.layoutContent, infoBookFragment)
                        .commitAllowingStateLoss()
                tagFragmentIsShowing = FRAGMENT_INFO_BOOK

                svSearchFood.visibility = View.GONE
                tvName.visibility = View.VISIBLE
                tvName.text = activityParent.resources.getString(R.string.dialog_choose_type_book)
            }

            FRAGMENT_INFO_DELIVERY -> {
                if (infoDeliveryFragment == null)
                    initInfoDeliveryFragment()
                fragmentTransaction.replace(R.id.layoutContent, infoDeliveryFragment)
                        .commitAllowingStateLoss()
                tagFragmentIsShowing = FRAGMENT_INFO_DELIVERY

                svSearchFood.visibility = View.GONE
                tvName.visibility = View.VISIBLE
                tvName.text = activityParent.resources.getString(R.string.dialog_choose_type_delivery)
            }
        }
    }

    private fun initChooseOrderFragment() {
        chooseOrderFragment = ChooseOrderFragment()
        //put data
        val dataSend = Bundle()
        dataSend.putSerializable(LIST_ORDER_FOOD_SEND, listOrderFood)

        chooseOrderFragment!!.arguments = dataSend
        chooseOrderFragment!!.onItemBottomNavigationClickListener = this
    }

    private fun initConfirmOrderFragment() {
        confirmOrderFragment = ConfirmOrderFragment()
        //put data
        val dataSend = Bundle()
        dataSend.putSerializable(LIST_ORDER_FOOD_SEND, listOrderFood)
        confirmOrderFragment!!.arguments = dataSend
        confirmOrderFragment!!.onItemBottomNavigationClickListener = this
        confirmOrderFragment!!.onCreateOrderCallBack = object : ConfirmOrderFragment.CreateOrderCallBack {
            override fun onCreate(orderNew: CreateOrder) {
                doCreateOrder(orderNew)
            }

            override fun onAddFood(addFoodToOrder: AddFoodToOrder) {
                doAddFood(addFoodToOrder)
            }
        }
    }

    private fun initSearchFragment() {
        searchFoodFragment = SearchFoodFragment()
        //put data
        val dataSend = Bundle()
        dataSend.putSerializable(LIST_ORDER_FOOD_SEND, listOrderFood)
        searchFoodFragment!!.arguments = dataSend
        //set listener
        searchFoodFragment!!.onChangeNumberListener = object : ShowMenuItemAdapter.OnChangeNumberListener {
            override fun onChangeNumberOrder(index: Int, oldValue: Int, newValue: Int) {
                //nothing to do
            }
        }
    }

    private fun initInfoBookFragment() {
        infoBookFragment = InfoBookFragment()
        val dataSend = Bundle()
        dataSend.putSerializable(LIST_ORDER_FOOD_SEND, listOrderFood)
        infoBookFragment!!.arguments = dataSend
        infoBookFragment!!.onItemBottomNavigationClickListener = this
        infoBookFragment!!.onCreateBookCallBack = object : InfoBookFragment.CreateBookCallBack {
            override fun onCreate(bookNew: CreateBook) {
                doCreateBook(bookNew)
            }
        }
    }

    private fun initInfoDeliveryFragment() {
        infoDeliveryFragment = InfoDeliveryFragment()
        val dataSend = Bundle()
        dataSend.putSerializable(LIST_ORDER_FOOD_SEND, listOrderFood)
        infoDeliveryFragment!!.arguments = dataSend
        infoDeliveryFragment!!.onItemBottomNavigationClickListener = this
        infoDeliveryFragment!!.onCreateDeliveryCallBack = object : InfoDeliveryFragment.CreateDeliveryCallBack {
            override fun onCreate(deliveryNew: CreateDelivery) {
                doCreateDelivery(deliveryNew)
            }
        }
    }

    private fun setUpActionBar() {
        tvName.text = Constraint.NAME_STORE_ORDERING
        if (Constraint.TYPE_USE == UseType.LOCAL_HAVE_TABLE)
            ibtnCallWaiter.visibility = View.VISIBLE
        else {
            ibtnCallWaiter.visibility = View.GONE

            val paramsSearch = svSearchFood.layoutParams as RelativeLayout.LayoutParams
            paramsSearch.apply {
                addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
                addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0)
                addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
                addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
                addRule(RelativeLayout.ALIGN_PARENT_START, 0)
                removeRule(RelativeLayout.START_OF)
                addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE)
                paramsSearch.width = RelativeLayout.LayoutParams.WRAP_CONTENT
            }

            svSearchFood.layoutParams = paramsSearch
        }
    }
}
