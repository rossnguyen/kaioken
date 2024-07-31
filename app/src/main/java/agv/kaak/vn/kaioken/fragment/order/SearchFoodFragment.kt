package agv.kaak.vn.kaioken.fragment.order


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.activity.OrderActivity
import agv.kaak.vn.kaioken.adapter.ShowMenuItemAdapter
import agv.kaak.vn.kaioken.entity.DetailOrder
import agv.kaak.vn.kaioken.entity.MenuCategoryPromotion
import agv.kaak.vn.kaioken.entity.MenuFood
import agv.kaak.vn.kaioken.entity.MenuInfo
import agv.kaak.vn.kaioken.entity.define.DiscountType
import agv.kaak.vn.kaioken.entity.define.OrderType
import agv.kaak.vn.kaioken.entity.define.PromotionType
import agv.kaak.vn.kaioken.entity.result.DetailStoreResult
import agv.kaak.vn.kaioken.fragment.base.BaseFragment
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.mvp.contract.DetailStoreContract
import agv.kaak.vn.kaioken.mvp.presenter.DetailStorePresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentManager
import android.util.Log
import android.view.Menu
import android.widget.Toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SearchFoodFragment : BaseFragment(), DetailStoreContract.View {

    val presenter = DetailStorePresenter(this)
    var detailStore: DetailStoreResult? = null
    private lateinit var listOrderFood: ArrayList<DetailOrder>
    lateinit var menu: ArrayList<MenuInfo>
    private lateinit var listFood: ArrayList<MenuFood>

    var fragmentItemFood: ShowMenuItemFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_food, container, false)
    }

    override fun getData() {
        listOrderFood = arguments!!.getSerializable(OrderFragment.LIST_ORDER_FOOD_SEND) as ArrayList<DetailOrder>
    }

    override fun loadData() {
        presenter.getDetailStore(Constraint.ID_STORE_ORDERING, Constraint.uid!!.toInt())
    }

    override fun addEvent() {
        //nothing to do
    }

    private fun getAllFoodFromMenu(menu: ArrayList<MenuInfo>): ArrayList<MenuFood> {
        val result: ArrayList<MenuFood> = arrayListOf()
        for (eachCategory in menu) {
            result.addAll(eachCategory.items!!)
        }

        return result
    }

    private fun initListMenu() {
        fragmentItemFood = ShowMenuItemFragment()
        val bundle = Bundle()
        bundle.putSerializable(ShowMenuFragment.LIST_ITEM, listFood)
        bundle.putSerializable(ChooseOrderFragment.LIST_ORDER, listOrderFood)
        bundle.putSerializable(ChooseOrderFragment.TYPE, OrderType.TYPE_CHOSSE_FOOD)
        fragmentItemFood?.arguments = bundle

        fragmentItemFood?.onChangeNumberListener = onChangeNumberListener
        fragmentManager?.beginTransaction()?.replace(R.id.layoutContent, fragmentItemFood)?.commit()
    }

    fun searchFood(query: String) {
        fragmentItemFood?.doFilter(query)
    }

    override fun getDetailStoreSuccess(data: DetailStoreResult) {
        detailStore = data
        applyPromotion()
        menu = detailStore!!.menuInfo as ArrayList<MenuInfo>
        listFood = getAllFoodFromMenu(menu)
        initListMenu()
    }

    private fun applyPromotion() {
        if (detailStore!!.promotion != null) {
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

        listCategory?.forEach { categoryPromotion ->
            detailStore!!.menuInfo!!.forEach { menuInfo ->
                if (menuInfo.categoryId == categoryPromotion.menuCategoryId) {
                    menuInfo.items!!.forEach { menuFoodHadPromotion ->
                        if (OrderFragment.storePromotion?.discountType!! == DiscountType.VALUE)
                            menuFoodHadPromotion.priceAfterPromotion = menuFoodHadPromotion.price - OrderFragment.storePromotion?.discountValue!!
                        else {
                            val priceDiscount = Math.floor(menuFoodHadPromotion.price * OrderFragment.storePromotion?.discountValue!! / 100)
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
    }

    private fun updatePriceViaPromotionFood(listMenu: ArrayList<MenuFood>?) {
        OrderFragment.isApplyPromotionAllOrder = false

        listMenu?.forEach { menuHadPromotion ->
            detailStore!!.menuInfo?.forEach { category ->
                category.items?.forEach { food ->
                    if (food.id == menuHadPromotion.menuItemId) {
                        if (OrderFragment.storePromotion?.discountType!! == DiscountType.VALUE)
                            food.priceAfterPromotion = food.price - OrderFragment.storePromotion?.discountValue!!
                        else {
                            val priceDiscount = Math.floor(food.price * OrderFragment.storePromotion?.discountValue!! / 100)
                            food.priceAfterPromotion = food.price - priceDiscount
                        }
                    } else
                        food.priceAfterPromotion = food.price
                }
            }
        }
    }

    override fun getDetailStoreFail(msg: String?) {
        GlobalHelper.showMessage(mContext, msg!!, true)
    }

    lateinit var onChangeNumberListener: ShowMenuItemAdapter.OnChangeNumberListener
}
