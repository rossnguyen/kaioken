package agv.kaak.vn.kaioken.entity

import agv.kaak.vn.kaioken.helper.ConvertHelper
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class CreateDelivery : Serializable {
    @SerializedName("customer_id")
    var customerId: String? = ""

    @SerializedName("customer_order_phone")
    var phoneNumber: String? = ""

    @SerializedName("delivery_address")
    var address: String? = ""

    @SerializedName("delivery_location")
    var location: String? = ""

    @SerializedName("delivery_time_arrive")
    var canWaitTo: String? = null

    @SerializedName("coupon_code")
    var discountCode: String? = null

    @SerializedName("order_note")
    var note: String? = ""

    @SerializedName("order_type")
    var orderType: Int? = 1

    @SerializedName("page_id")
    var pageId: String? = ""

    @SerializedName("payment_type")
    var paymentType: String? = ""

    @SerializedName("discount_name")
    var discountName: String? = ""

    @SerializedName("discount_type")
    var discountType: String? = ""

    @SerializedName("discount_value")
    var discountValue: String? = ""

    @SerializedName("notification_email")
    var notificationEmail:String?=""

    @SerializedName("items")
    var listOrder: ArrayList<DetailOrder>? = arrayListOf()


    constructor()
    constructor(customerId: String?, phoneNumber: String?, address: String?, location: String?, canWaitTo: Date?, discountCode: String?, note: String?, orderType: Int?, pageId: String?, paymentType: String?, discountName: String?, discountType: String?, discountValue: String?,notificationEmail:String?, listOrder: ArrayList<DetailOrder>?) {
        this.customerId = customerId
        this.phoneNumber = phoneNumber
        this.address = address
        this.location = location
        this.canWaitTo = ConvertHelper.dateTimeToStringGlobal(canWaitTo)
        this.discountCode = discountCode
        this.note = note
        this.orderType = orderType
        this.pageId = pageId
        this.paymentType = paymentType
        this.discountName = discountName
        this.discountType = discountType
        this.discountValue = discountValue
        this.notificationEmail=notificationEmail
        this.listOrder = listOrder
    }


    fun newInstance(): CreateDelivery {
        val result = CreateDelivery()
        result.customerId = this.customerId
        result.phoneNumber = this.phoneNumber
        result.address = this.address
        result.location = this.location
        result.canWaitTo = canWaitTo
        result.discountCode = this.discountCode
        result.note = this.note
        result.orderType = this.orderType
        result.pageId = this.pageId
        result.paymentType = this.paymentType
        result.discountName = this.discountName
        result.discountType = this.discountType
        result.discountValue = this.discountValue
        result.notificationEmail=this.notificationEmail
        result.listOrder = this.listOrder
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