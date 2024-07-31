package agv.kaak.vn.kaioken.entity

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable

class CreateOrder:Serializable {
    @SerializedName("customer_id")
    var customerId: Int? = 0
    @SerializedName("discount_name")
    var discountReason: String?=""
    @SerializedName("discount_type")
    var discountType: Int? = 0
    @SerializedName("discount_value")
    var discountValue: Int? = 0
    @SerializedName("items")
    var listOrder: ArrayList<DetailOrder> = arrayListOf()
    @SerializedName("notification_email")
    var notification_email: String?=""
    @SerializedName("order_note")
    var note: String?=""
    @SerializedName("order_type")
    var orderType: Int? = 0
    @SerializedName("page_id")
    var pageId: Int? = 0
    @SerializedName("payment_type")
    var paymenType: Int? = 0
    @SerializedName("table_id")
    var tableId: Int? = 0
    @SerializedName("is_staff_order")
    var isStaffOrder: Int? = 0
    @SerializedName("coupon_code")
    var couponCode:String?=""

    constructor(customerId: Int?, discountReason: String?, discountType: Int?, discountValue: Int?, listOrder: ArrayList<DetailOrder>, notification_email: String?, note: String?, orderType: Int?, pageId: Int?, paymenType: Int?, tableId: Int?, isStaffOrder: Int?) {
        this.customerId = customerId
        this.discountReason = discountReason
        this.discountType = discountType
        this.discountValue = discountValue
        this.listOrder = listOrder
        this.notification_email = notification_email
        this.note = note
        this.orderType = orderType
        this.pageId = pageId
        this.paymenType = paymenType
        this.tableId = tableId
        this.isStaffOrder = isStaffOrder
    }

    constructor()


    fun newInstance(): CreateOrder {
        val result = CreateOrder()
        result.customerId = this.customerId
        result.discountReason = this.discountReason
        result.discountType = this.discountType
        result.discountValue = this.discountValue
        result.listOrder = this.listOrder
        result.notification_email = this.notification_email
        result.note = this.note
        result.orderType = this.orderType
        result.pageId = this.pageId
        result.paymenType = this.paymenType
        result.tableId = this.tableId
        result.isStaffOrder = this.isStaffOrder

        return result
    }

    fun toJsonObject(): JSONObject? {
        var result: JSONObject? = null
        val gson = Gson()
        try {
            result = JSONObject(gson.toJson(newInstance()))
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return result
    }
}