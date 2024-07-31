package agv.kaak.vn.kaioken.entity

import agv.kaak.vn.kaioken.utils.Constraint
import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.io.Serializable

class DetailOrder : Serializable {
    @SerializedName("discount_type")
    var discountType: Int? = 0

    @SerializedName("discount_value")
    var discountValue: Int? = 0

    @SerializedName("item_id")
    var idFood: Int? = 0

    @SerializedName("item_quantity")
    var numberOrder: Int? = 0

    @SerializedName("order_detail_note")
    var note: String? = ""

    @SerializedName("name")
    var nameFood: String? = ""

    @SerializedName("image")
    var linkImage: String? = null

    @SerializedName("price_before")
    var priceBefore: Double? = 0.0

    @SerializedName("price_after")
    var priceAfter: Double? = 0.0

    @SerializedName("total_price")
    var totalPrice: Double? = 0.0

    @SerializedName("customer_name")
    var customerName: String? = ""

    @SerializedName("staff_event")
    var staffEvent: Int? = -1

    @SerializedName("event_status")
    var eventStatus: Int? = null

    @SerializedName("childs")
    var listTopping: ArrayList<ToppingForSocket> = arrayListOf()

    @SerializedName("childs_desc")
    var childs_desc: String? = ""

    @SerializedName("parent_id")
    var parentId: Int? = -1

    @SerializedName("size_name")
    var sizeName: String? = ""

    constructor()

    constructor(discountType: Int?, discountValue: Int?, idFood: Int?, numberOrder: Int?, note: String?, nameFood: String?, linkImage: String?, priceAfter: Double?, customerName: String?, staffEvent: Int?, eventStatus: Int?, parentId: Int?, sizeName: String?) {
        this.discountType = discountType
        this.discountValue = discountValue
        this.idFood = idFood
        this.numberOrder = numberOrder
        this.note = note
        this.nameFood = nameFood
        this.linkImage = linkImage
        this.priceAfter = priceAfter
        this.customerName = customerName
        this.staffEvent = staffEvent
        this.eventStatus = eventStatus
        this.parentId = parentId
        this.sizeName = sizeName
    }

    fun newInstance(): DetailOrder {
        val result = DetailOrder()
        result.discountType = this.discountType
        result.discountValue = this.discountValue
        result.idFood = this.idFood
        result.numberOrder = this.numberOrder
        result.note = this.note
        result.nameFood = this.nameFood
        result.linkImage = this.linkImage
        result.priceAfter = this.priceAfter
        result.customerName = this.customerName
        result.staffEvent = this.staffEvent
        result.eventStatus = this.eventStatus
        result.listTopping = this.listTopping
        result.parentId = this.parentId

        return result
    }


    companion object {
        fun fromJsonObject(jsonObject: JSONObject): DetailOrder {
            return Gson().fromJson(jsonObject.toString(), DetailOrder::class.java)
        }

        fun addItemIntoListOrder(item: DetailOrder, listOrder: ArrayList<DetailOrder>) {
            if (listOrder.isEmpty())
                listOrder.add(item)
            else {
                var existItem = false

                for (i in 0 until listOrder.size)
                    if (listOrder[i].getString() == item.getString()) {
                        listOrder[i].numberOrder = listOrder[i].numberOrder!! + item.numberOrder!!
                        existItem = true
                        break
                    }
                if (!existItem)
                    listOrder.add(item)
            }
        }

        fun minusItemFromListOrder(indexItem: Int, listOrder: ArrayList<DetailOrder>) {
            val itemOrder = listOrder[indexItem]
            val quantity = itemOrder.numberOrder!!
            if (quantity > 1)
                itemOrder.numberOrder = quantity - 1
            else
                listOrder.removeAt(indexItem)
        }

        fun getQuantityViaFood(listOrdering: ArrayList<DetailOrder>, food: MenuFood): Int {
            var sum = 0
            listOrdering.forEach {
                if (it.parentId == food.id)
                    sum += it.numberOrder!!
            }
            return sum
        }

        fun getSumQuantity(listOrdering: ArrayList<DetailOrder>): Int {
            var sum = 0
            listOrdering.forEach {
                sum += it.numberOrder!!
            }
            return sum
        }

        fun getSumPrice(listOrdering: ArrayList<DetailOrder>): Double {
            var sum = 0.toDouble()
            listOrdering.forEach {
                sum += it.getPrice() * it.numberOrder!!
            }

            return sum
        }

        fun removeEmptyItemFromListOrder(listOrder: ArrayList<DetailOrder>) {
            val listEmpty = arrayListOf<Int>()
            listOrder.forEachIndexed { index, detailOrder ->
                if (detailOrder.numberOrder == 0)
                    listEmpty.add(index)
            }

            for (i in 10 downTo 1) {
                Log.d("****TestFor", "$i")
            }

            for (i in listEmpty.size - 1 downTo 0) {
                listOrder.removeAt(listEmpty[i])
            }
        }
    }

    fun getPrice(): Double {
        var totalPrice = 0.toDouble()
        totalPrice += priceAfter!!
        listTopping.forEach {
            totalPrice += it.priceAfter!!
        }

        return totalPrice
    }

    fun getStringTopping(): String {
        var toppingName = ""
        listTopping.forEach {
            if (toppingName.isEmpty())
                toppingName += it.nameTopping
            else
                toppingName += ", ${it.nameTopping}"
        }

        return toppingName
    }

    fun getString() = "$idFood${Constraint.splitCharacters}$note${Constraint.splitCharacters}${getStringTopping()}"
}