package agv.kaak.vn.kaioken.entity

import agv.kaak.vn.kaioken.helper.ConvertHelper
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class CreateBook:Serializable {
    @SerializedName("customer_id")
    var customerId:String?=""

    @SerializedName("customer_order_phone")
    var numberPhone:String?=""
    
    @SerializedName("coupon_code")
    var discountCode:String?=""
    
    @SerializedName("order_note")
    var note:String?=""
    
    @SerializedName("order_table_number")
    var numberTable:String?=""
    
    @SerializedName("order_table_time_arrive")
    var timeArrival: String?=""
    
    @SerializedName("order_type")
    var orderType:String?=""
    
    @SerializedName("page_id")
    var pageId:String?=""
    
    @SerializedName("payment_type")
    var paymentType:String?=""

    @SerializedName("discount_name")
    var discountName:String?=""

    @SerializedName("discount_type")
    var discountType:String?=""

    @SerializedName("discount_value")
    var discountValue:String?=""

    @SerializedName("notification_email")
    var notificationEmail:String?=""

    @SerializedName("items")
    var listOrder: ArrayList<DetailOrder>? = arrayListOf()

    constructor()
    constructor(customerId: String?, numberPhone: String?, discountCode: String?, note: String?, numberTable: String?, timeArrival: Date?, orderType: String?, pageId: String?, paymentType: String?, discountName: String?, discountType: String?, discountValue: String?, notificationEmail: String?, listOrder: ArrayList<DetailOrder>?) {
        this.customerId = customerId
        this.numberPhone = numberPhone
        this.discountCode = discountCode
        this.note = note
        this.numberTable = numberTable
        this.timeArrival = ConvertHelper.dateTimeToStringGlobal(timeArrival)
        this.orderType = orderType
        this.pageId = pageId
        this.paymentType = paymentType
        this.discountName = discountName
        this.discountType = discountType
        this.discountValue = discountValue
        this.notificationEmail = notificationEmail
        this.listOrder = listOrder
    }


    fun newInstance(): CreateBook {
        val result = CreateBook()

        result.customerId = this.customerId
        result.numberPhone = this.numberPhone
        result.discountCode = this.discountCode
        result.note = this.note
        result.numberTable = this.numberTable
        result.timeArrival = this.timeArrival
        result.orderType = this.orderType
        result.pageId = this.pageId
        result.paymentType = this.paymentType
        result.discountName=this.discountName
        result.discountType = this.discountType
        result.discountValue = this.discountValue
        result.notificationEmail = this.notificationEmail
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