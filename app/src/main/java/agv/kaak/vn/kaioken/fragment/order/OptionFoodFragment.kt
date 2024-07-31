package agv.kaak.vn.kaioken.fragment.order

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.adapter.SizeAdapter
import agv.kaak.vn.kaioken.adapter.ToppingAdapter
import agv.kaak.vn.kaioken.entity.DetailOrder
import agv.kaak.vn.kaioken.entity.MenuFood
import agv.kaak.vn.kaioken.entity.SizeFood
import agv.kaak.vn.kaioken.entity.ToppingForSocket
import agv.kaak.vn.kaioken.entity.define.DiscountType
import agv.kaak.vn.kaioken.entity.define.PlaceHolderType
import agv.kaak.vn.kaioken.fragment.base.BaseBottomSheetDialogFragment
import agv.kaak.vn.kaioken.helper.ConvertHelper
import agv.kaak.vn.kaioken.helper.ImageHelper
import agv.kaak.vn.kaioken.mvp.contract.MenuItemDetailContract
import agv.kaak.vn.kaioken.mvp.presenter.MenuItemDetailPresenter
import agv.kaak.vn.kaioken.utils.Constraint
import android.content.res.ColorStateList
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_option_food.*
import kotlinx.android.synthetic.main.item_show_menu_item.*
import kotlin.math.roundToInt


class OptionFoodFragment : BaseBottomSheetDialogFragment(), MenuItemDetailContract.View {

    var idFood = -1
    lateinit var foodWithTopping: MenuFood
    private var adapterTopping: ToppingAdapter? = null
    private var adapterSize: SizeAdapter? = null
    val presenter = MenuItemDetailPresenter(this)
    lateinit var newOrderItem: DetailOrder
    var discountType = DiscountType.VALUE
    var discountValue = 0

    private lateinit var mBehavior: BottomSheetBehavior<View>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_option_food, container, false)
    }

    override fun onStart() {
        super.onStart()
        mBehavior = BottomSheetBehavior.from(view!!.parent as View)
        mBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun getData() {
        idFood = arguments!!.getInt(Constraint.DATA_SEND)
    }

    override fun addEvent() {

        ibtnPlus.setOnClickListener {
            enableButtonAddToCart()
            val oldValue = tvQuantity.text.toString().toInt()
            val newValue = oldValue + 1
            newOrderItem.numberOrder = newValue
            tvQuantity.text = newValue.toString()
            updateSumMoney()
        }

        ibtnMinus.setOnClickListener {
            val oldValue = tvQuantity.text.toString().toInt()
            if (oldValue == 0)
                return@setOnClickListener

            val newValue = oldValue - 1
            if (newValue == 0)
                disableButtonAddToCart()
            else
                enableButtonAddToCart()

            newOrderItem.numberOrder = newValue
            tvQuantity.text = newValue.toString()
            updateSumMoney()
        }

        btnCloseAddFood.setOnClickListener {
            dismiss()
        }

        layoutAddToCart.setOnClickListener {
            newOrderItem.note = etNoteOrderItem.text.toString()
            onAddFoodToCartClickListener?.onClick(newOrderItem)
            dismiss()
        }
    }

    override fun loadData() {
        tvQuantity.text = "1"
        presenter.getMenuItemDetail(idFood)
    }

    override fun dialogIsExpand() = true

    private fun initListTopping() {
        if (foodWithTopping.listTopping.isNotEmpty()) {
            layoutOptionTopping.visibility = View.VISIBLE

            adapterTopping = ToppingAdapter(mContext, foodWithTopping.listTopping)
            adapterTopping?.onToppingCheckedChange = object : ToppingAdapter.OnToppingCheckedChange {
                override fun onChange(isChecked: Boolean, topping: MenuFood) {
                    if (isChecked)
                        addTopping(topping)
                    else
                        removeTopping(topping)

                    updateSumMoney()
                }
            }

            val layoutManager = LinearLayoutManager(context)
            layoutManager.orientation = LinearLayoutManager.VERTICAL

            lstTopping.adapter = adapterTopping
            lstTopping.layoutManager = layoutManager
        }
    }

    private fun initListSize() {
        if (foodWithTopping.listSize.isNotEmpty()) {
            layoutOptionSize.visibility = View.VISIBLE

            adapterSize = SizeAdapter(mContext, foodWithTopping.listSize)
            adapterSize?.onSizeCheckedChange = object : SizeAdapter.OnSizeCheckedChange {
                override fun onChange(positionChecked: Int, sizeInfo: SizeFood) {
                    for (i in 0 until foodWithTopping.listSize.size) {
                        if (i != positionChecked)
                            (lstSize.findViewHolderForLayoutPosition(i) as SizeAdapter.ViewHolder).unCheck()

                        newOrderItem.priceAfter = sizeInfo.priceAfterPromotion
                        newOrderItem.idFood = sizeInfo.id
                        newOrderItem.sizeName = sizeInfo.sizeName
                        tvPriceAfterPromotion.text = ConvertHelper.doubleToMoney(mContext, sizeInfo.priceAfterPromotion)
                        updateSumMoney()
                    }
                }
            }

            val layoutManager = LinearLayoutManager(context)
            layoutManager.orientation = LinearLayoutManager.VERTICAL

            lstSize.adapter = adapterSize
            lstSize.layoutManager = layoutManager
        }
    }

    private fun bindData() {
        ImageHelper.loadImage(mContext, imgAvatar, foodWithTopping.image, PlaceHolderType.FOOD)
        tvName.text = foodWithTopping.name

        tvPriceAfterPromotion.text = ConvertHelper.doubleToMoney(mContext, foodWithTopping.priceAfterPromotion)
    }

    private fun initNewOrder() {
        newOrderItem = DetailOrder(1,
                0,
                foodWithTopping.menuItemId,
                1,
                "",
                foodWithTopping.name, foodWithTopping.image, foodWithTopping.priceAfterPromotion,
                "",
                1,
                1,
                foodWithTopping.parentId,
                "")
    }

    private fun addTopping(topping: MenuFood) {
        newOrderItem.listTopping.add(ToppingForSocket(topping.id,
                topping.name!!,
                topping.price))

        Log.d("****Topping", "Add: ${topping.name} (${newOrderItem.listTopping.size})")
    }

    private fun removeTopping(topping: MenuFood) {
        var indexTopping = -1

        newOrderItem.listTopping.forEachIndexed { index, toppingForSocket ->
            if (toppingForSocket.itemId == topping.id)
                indexTopping = index
        }
        if (indexTopping != -1)
            newOrderItem.listTopping.removeAt(indexTopping)

        Log.d("****Topping", "Remove: ${topping.name} (${newOrderItem.listTopping.size})")
    }

    private fun updateSumMoney() {
        var sum = newOrderItem.getPrice()
        sum *= newOrderItem.numberOrder!!
        tvSumItem.text = ConvertHelper.doubleToMoney(mContext, sum)

        if (sum == 0.toDouble()) {
            layoutAddToCart?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorGrayAppDark))
        } else {
            layoutAddToCart?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorVioletPrimary))
        }
    }

    private fun selectedFirsSize() {
        if (foodWithTopping.listSize.isNotEmpty()) {
            Handler().postDelayed({
                (lstSize.findViewHolderForLayoutPosition(0) as SizeAdapter.ViewHolder).checked()
            }, 1)
        }
    }

    override fun getMenuItemDetailSuccess(data: MenuFood) {
        foodWithTopping = data
        foodWithTopping.setId(idFood.toString())
        applyPromotionForSize(foodWithTopping)
        initNewOrder()
        bindData()
        initListTopping()
        initListSize()
        selectedFirsSize()
        updateSumMoney()
    }

    override fun getMenuItemDetailFail(message: String) {}

    private fun applyPromotionForSize(food: MenuFood) {
        if (OrderFragment.storePromotion != null && !OrderFragment.isApplyPromotionAllOrder && food.isPromotion == 1) {
            var priceDiscount = 0.toDouble()

            priceDiscount = if (OrderFragment.storePromotion?.discountType == DiscountType.VALUE)
                OrderFragment.storePromotion?.discountValue!!.toDouble()
            else
                food.price * OrderFragment.storePromotion?.discountValue!! / 100

            food.priceAfterPromotion = food.price - priceDiscount

            if (food.listSize.isNotEmpty()) {
                food.listSize.forEach { eachSize ->
                    priceDiscount = 0.toDouble()

                    priceDiscount = if (OrderFragment.storePromotion?.discountType == DiscountType.VALUE)
                        OrderFragment.storePromotion?.discountValue!!.toDouble()
                    else
                        eachSize.price * OrderFragment.storePromotion?.discountValue!! / 100

                    eachSize.priceAfterPromotion = eachSize.price - priceDiscount
                }
            }
        } else {
            food.priceAfterPromotion = food.priceMin
            if (food.listSize.isNotEmpty()) {
                food.listSize.forEach {
                    it.priceAfterPromotion = it.price
                }
            }
        }

    }

    private fun disableButtonAddToCart() {
        layoutAddToCart.isEnabled = false
    }

    private fun enableButtonAddToCart() {
        layoutAddToCart.isEnabled = true
    }

    var onAddFoodToCartClickListener: OnAddFoodToCartClickListener? = null

    interface OnAddFoodToCartClickListener {
        fun onClick(newOrder: DetailOrder)
    }
}