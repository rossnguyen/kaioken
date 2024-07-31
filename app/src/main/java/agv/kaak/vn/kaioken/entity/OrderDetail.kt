package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*


/**
 * Created by shakutara on 08/01/2018.
 */
class OrderDetail : Serializable {

    @SerializedName("order_detail_id")
    @Expose
    var orderDetailId: Int? = null
    @SerializedName("item_id")
    @Expose
    var itemId: Int? = null
    @SerializedName("item_quantity")
    @Expose
    var itemQuantity: Int? = null
    @SerializedName("order_id")
    @Expose
    var orderId: Int? = null
    @SerializedName("price_before")
    @Expose
    var priceBefore: Int? = null
    @SerializedName("price_after")
    @Expose
    var priceAfter: Int? = null
    @SerializedName("discount_type")
    @Expose
    var discountType: Int? = null
    @SerializedName("discount_value")
    @Expose
    var discountValue: Int? = null
    @SerializedName("item_status")
    @Expose
    var itemStatus: Int? = null
    @SerializedName("status_id")
    @Expose
    var statusId: Int? = null
    @SerializedName("event_status")
    @Expose
    var eventStatus: Int? = null
    @SerializedName("reason_event")
    @Expose
    var reasonEvent: String? = null
    @SerializedName("invoice_no")
    @Expose
    var invoiceNo: String? = null
    @SerializedName("staff_event")
    @Expose
    var staffEvent: Int? = null
    @SerializedName("customer_id")
    @Expose
    var customerId: Int? = null
    @SerializedName("order_detail_note")
    @Expose
    var orderDetailNote: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null
    @SerializedName("amount")
    @Expose
    var amount: Int? = null
    @SerializedName("date_chef_confirm")
    @Expose
    var dateChefConfirm: String? = null
    @SerializedName("date_complete")
    @Expose
    var dateComplete: String? = null
    @SerializedName("date_add")
    @Expose
    var dateAdd: String? = null
    @SerializedName("interval_chef_time")
    @Expose
    var intervalChefTime: Int? = null
    @SerializedName("payment_type")
    @Expose
    var paymentType: Int? = null
    @SerializedName("payment_status")
    @Expose
    var paymentStatus: Int? = null
    @SerializedName("money_receive_customer")
    @Expose
    var moneyReceiveCustomer: Double? = null
    @SerializedName("money_change")
    @Expose
    var moneyChange: Double? = null
    @SerializedName("vat")
    @Expose
    var VAT: Float? = null
    @SerializedName("extra_value")
    @Expose
    var extraValue: Int? = null
    @SerializedName("page_id")
    @Expose
    var pageId: Int? = -1
    @SerializedName("order_note")
    @Expose
    var orderNote: String? = null
    @SerializedName("is_staff_order")
    @Expose
    var isStaffOrder: Int? = 0

}