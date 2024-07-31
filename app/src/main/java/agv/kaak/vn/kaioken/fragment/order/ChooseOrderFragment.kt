package agv.kaak.vn.kaioken.fragment.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.ShowMenuItemAdapter
import agv.kaak.vn.kaioken.dialog.DialogDetailPromotion
import agv.kaak.vn.kaioken.entity.DetailOrder
import agv.kaak.vn.kaioken.entity.MenuCategoryPromotion
import agv.kaak.vn.kaioken.entity.MenuFood
import agv.kaak.vn.kaioken.entity.MenuInfo
import agv.kaak.vn.kaioken.entity.define.*
import agv.kaak.vn.kaioken.entity.result.DetailStoreResult
import agv.kaak.vn.kaioken.fragment.base.BaseSocketFragment
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.DetailStoreContract
import agv.kaak.vn.kaioken.mvp.presenter.DetailStorePresenter
import agv.kaak.vn.kaioken.utils.Constraint
import agv.kaak.vn.kaioken.utils.implement.OnItemBottomNavigationClickListener
import android.content.res.ColorStateList
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.TextView
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import kotlinx.android.synthetic.main.fragment_choose_order.*
import kotlin.math.roundToInt

class ChooseOrderFragment : BaseSocketFragment(), DetailStoreContract.View, ShowMenuItemAdapter.OnChangeNumberListener {
    companion object {
        val SOURCE = "SOURCE"
        val LIST_ORDER = "LIST_ORDER"
        val TYPE = "TYPE"
    }

    lateinit var listOrderFood: ArrayList<DetailOrder>
    var detailStore: DetailStoreResult? = null

    lateinit var tabMenu: ShowMenuFragment

    lateinit var itemSumMoney: AHBottomNavigationItem

    private val detailStorePresenter = DetailStorePresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_order, container, false)
    }

    override fun getData() {
        listOrderFood = arguments!!.getSerializable(OrderFragment.LIST_ORDER_FOOD_SEND) as ArrayList<DetailOrder>
    }

    override fun loadData() {
        setTextNavigation()
        if (GlobalHelper.networkIsConnected(activityParent)) {
            detailStorePresenter.getDetailStore(Constraint.ID_STORE_ORDERING, Constraint.uid!!.toInt())
            layoutLoading.visibility = View.VISIBLE
        }
    }

    override fun addEvent() {
        layoutChart.setOnClickListener {
            if (listOrderFood.isEmpty()) {
                GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.choose_food_at_least_one_item), true)
            } else {
                when (Constraint.TYPE_USE) {
                    UseType.LOCAL_HAVE_TABLE -> onItemBottomNavigationClickListener?.onClick(BottomNavigationCode.GO_TO_CONFIRM)
                    UseType.BOOK -> onItemBottomNavigationClickListener?.onClick(BottomNavigationCode.CONFIRM_BOOK)
                    UseType.DELIVERY -> onItemBottomNavigationClickListener?.onClick(BottomNavigationCode.CONFIRM_DELIVERY)
                }
            }
        }
    }

    override fun listenSocket() {
        //nothing to do
    }

    override fun offSocket() {
        //nothing to do
    }

    override fun setupComponentStatusSocket(): TextView? {
        return null
    }

    private fun setTextNavigation() {
        when (Constraint.TYPE_USE) {
            UseType.LOCAL_HAVE_TABLE -> tvTitleNavigate.text = activityParent.resources.getString(R.string.choose_food_see_chart)
            UseType.BOOK -> tvTitleNavigate.text = activityParent.resources.getString(R.string.choose_food_enter_info_book)
            UseType.DELIVERY -> tvTitleNavigate.text = activityParent.resources.getString(R.string.choose_food_enter_info_delivery)
        }
    }

    private fun initTabMenu(menu: ArrayList<MenuInfo>, listOrder: ArrayList<DetailOrder>) {
        //for menu tab
        tabMenu = ShowMenuFragment()
        val dataForMenu = Bundle()
        dataForMenu.putSerializable(LIST_ORDER, listOrder)
        dataForMenu.putSerializable(SOURCE, menu)
        dataForMenu.putInt(TYPE, OrderType.TYPE_CHOSSE_FOOD)
        tabMenu.arguments = dataForMenu
        tabMenu.onChangeNumberListener = this
    }

    private fun bindMenu() {
        initTabMenu(detailStore!!.menuInfo as ArrayList<MenuInfo>, listOrderFood)
        mFragmentManager.beginTransaction().replace(R.id.layoutChooseOrder, tabMenu).commitAllowingStateLoss()
    }

    private fun setSumMoney() {
        val sumMoney = DetailOrder.getSumPrice(listOrderFood)
        val sumQuantity = DetailOrder.getSumQuantity(listOrderFood)

        if (sumMoney == 0.toDouble()) {
            tvSummaryQuantity?.text = "0"
            layoutChart?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorGrayAppDark))
            tvSumMoney?.text = activityParent.resources.getString(R.string.format_x_k, 0)
        } else {
            layoutChart?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorVioletPrimary))
            tvSummaryQuantity?.text = "$sumQuantity"
            tvSumMoney?.text = ConvertHelper.doubleToMoney(mContext, sumMoney)
        }

        layoutBottomNavigate?.refresh()
    }

    override fun getDetailStoreSuccess(data: DetailStoreResult) {
        //show dialog detail promotion if exist promotion
        if (detailStore == null && data.promotion != null) {
            val dialogDetailPromotion = DialogDetailPromotion()
            val bundle = Bundle()
            bundle.putInt(Constraint.DATA_SEND, data.id)
            bundle.putString(DialogDetailPromotion.NAME_SEND, data.name)
            bundle.putString(DialogDetailPromotion.LINK_IMAGE_SEND, data.cover)
            dialogDetailPromotion.arguments = bundle
            dialogDetailPromotion.show(mFragmentManager, "PROMOTION")
        }

        detailStore = data
        OrderFragment.storePromotion = detailStore?.promotion

        if (detailStore != null) {
            applyPromotion()
            bindMenu()
        } else
            OrderFragment.isApplyPromotionAllOrder = false

        setSumMoney()
        layoutLoading?.visibility = View.GONE
    }

    private fun applyPromotion() {
        if (detailStore!!.promotion != null && detailStore!!.promotion!!.isAuto == 1) {
            when (detailStore!!.promotion!!.typeId) {
                PromotionType.FOR_ORDER -> updatePriceViaPromotionOrder()

                PromotionType.FOR_CATEGORY -> updatePriceViaPromotionCategory(detailStore!!.promotion!!.listMenuCategory)

                PromotionType.FOR_FOOD -> updatePriceViaPromotionFood(detailStore!!.promotion!!.listMenuItem)
            }
        } else {
            detailStore!!.menuInfo?.forEach { menuInfo ->
                menuInfo.items?.forEach { food ->
                    food.priceAfterPromotion = food.price
                }
            }
        }
    }

    private fun updatePriceViaPromotionOrder() {
        OrderFragment.isApplyPromotionAllOrder = true

        detailStore!!.menuInfo!!.forEach { menuInfo ->
            menuInfo.items!!.forEach { food ->
                food.priceAfterPromotion = food.price
            }
        }
    }

    private fun updatePriceViaPromotionCategory(listCategory: ArrayList<MenuCategoryPromotion>?) {
        OrderFragment.isApplyPromotionAllOrder = false

        detailStore!!.menuInfo!!.forEach { menuInfo ->
            listCategory?.forEach { categoryPromotion ->
                if (menuInfo.categoryId == categoryPromotion.menuCategoryId) {
                    menuInfo.items!!.forEach { menuFoodHadPromotion ->
                        if (OrderFragment.storePromotion?.discountType!! == DiscountType.VALUE)
                            menuFoodHadPromotion.priceAfterPromotion = menuFoodHadPromotion.price - OrderFragment.storePromotion?.discountValue!!
                        else {
                            val priceDiscount = menuFoodHadPromotion.price * OrderFragment.storePromotion?.discountValue!! / 100
                            menuFoodHadPromotion.priceAfterPromotion = menuFoodHadPromotion.price - priceDiscount
                        }

                        Log.d("****ApplyPromotion", "${menuFoodHadPromotion.name} - ${menuFoodHadPromotion.priceAfterPromotion}")
                    }
                } else {
                    menuInfo.items!!.forEach { menuFood ->
                        if (menuFood.priceAfterPromotion == -1.toDouble())
                            menuFood.priceAfterPromotion = menuFood.price
                    }
                }
            }
        }

        /*listCategory?.forEach { categoryPromotion ->
            detailStore!!.menuInfo!!.forEach { menuInfo ->
                if (menuInfo.categoryId == categoryPromotion.menuCategoryId) {
                    menuInfo.items!!.forEach { menuFoodHadPromotion ->
                        if (OrderFragment.storePromotion?.discountType!! == DiscountType.VALUE)
                            menuFoodHadPromotion.priceAfterPromotion = menuFoodHadPromotion.price - OrderFragment.storePromotion?.discountValue!!
                        else {
                            val priceDiscount = menuFoodHadPromotion.price * OrderFragment.storePromotion?.discountValue!! / 100
                            menuFoodHadPromotion.priceAfterPromotion = menuFoodHadPromotion.price - priceDiscount
                        }

                        Log.d("****ApplyPromotion", "${menuFoodHadPromotion.name} - ${menuFoodHadPromotion.priceAfterPromotion}")
                    }
                } else {
                    menuInfo.items!!.forEach { menuFood ->
                        if (menuFood.priceAfterPromotion == -1.toDouble())
                            menuFood.priceAfterPromotion = menuFood.price
                    }
                }
            }
        }*/
    }

    private fun updatePriceViaPromotionFood(listMenu: ArrayList<MenuFood>?) {
        OrderFragment.isApplyPromotionAllOrder = false

        detailStore!!.menuInfo?.forEach { category ->
            category.items?.forEach { food ->
                listMenu?.forEach { menuHadPromotion ->
                    if (food.id == menuHadPromotion.menuItemId) {
                        if (OrderFragment.storePromotion?.discountType!! == DiscountType.VALUE)
                            food.priceAfterPromotion = food.price - OrderFragment.storePromotion?.discountValue!!
                        else {
                            val priceDiscount = food.price * OrderFragment.storePromotion?.discountValue!! / 100
                            food.priceAfterPromotion = food.price - priceDiscount
                        }
                    } else
                        if (food.priceAfterPromotion == -1.toDouble())
                            food.priceAfterPromotion = food.price
                }
            }
        }
        /*listMenu?.forEach { menuHadPromotion ->
            detailStore!!.menuInfo?.forEach { category ->
                category.items?.forEach { food ->
                    if (food.id == menuHadPromotion.menuItemId) {
                        if (OrderFragment.storePromotion?.discountType!! == DiscountType.VALUE)
                            food.priceAfterPromotion = food.price - OrderFragment.storePromotion?.discountValue!!
                        else {
                            val priceDiscount = food.price * OrderFragment.storePromotion?.discountValue!! / 100
                            food.priceAfterPromotion = food.price - priceDiscount
                        }
                    } else
                        food.priceAfterPromotion = food.price
                }
            }
        }*/
    }

    override fun getDetailStoreFail(msg: String?) {
        GlobalHelper.showMessage(mContext, activityParent.resources.getString(R.string.all_get_data_fail_message), true)
        Log.e("****errorGetDetailStore", msg)
    }

    override fun onChangeNumberOrder(index: Int, oldValue: Int, newValue: Int) {
        setSumMoney()
    }

    var onItemBottomNavigationClickListener: OnItemBottomNavigationClickListener? = null
}
