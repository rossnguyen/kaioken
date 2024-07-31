package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DetailOrderInfo : Serializable {
    @SerializedName("order_id")
    var orderId: Int? = -1

    @SerializedName("order_type")
    var orderType: Int? = 0

    @SerializedName("customer_id")
    var customerId: Int? = -1

    @SerializedName("invoice_no")
    var invoiceNo: String? = ""

    @SerializedName("interval_chef_time")
    var intervalChefTime: Int? = -1

    @SerializedName("staff_confirm")
    var staffConfirm: String? = ""

    @SerializedName("cashier_confirm")
    var cashierConfirm: String? = ""

    @SerializedName("chef_confirm")
    var chefConfirm: String? = ""

    @SerializedName("status_id")
    var statusOrder: Int? = -1

    @SerializedName("page_id")
    var pageId: Int? = -1

    @SerializedName("table_id")
    var tableId: Int? = -1

    @SerializedName("date_chef_confirm")
    var timeChefConfirm: String? = null

    @SerializedName("price_before")
    var priceBefore: Double? = 0.toDouble()

    @SerializedName("price_after")
    var priceAfter: Double? = 0.toDouble()

    @SerializedName("discount_name")
    var discountName: String? = ""

    @SerializedName("discount_type")
    var discountType: Int? = -1

    @SerializedName("discount_value")
    var discountValue: Double? = 0.toDouble()

    @SerializedName("discount_final_match")
    var discountFinalMatch: Double? = 0.toDouble()

    @SerializedName("total_item_discount_value")
    var totalItemDiscountValue: Double? = 0.toDouble()

    @SerializedName("extra_name")
    var extraName: String? = ""

    @SerializedName("extra_value")
    var extraValue: Double? = 0.toDouble()

    @SerializedName("vat")
    var vat: Int? = 0

    @SerializedName("vat_value")
    var vatValue: Double? = 0.toDouble()

    @SerializedName("money_receive_customer")
    var moneyReceive: Double? = 0.toDouble()

    @SerializedName("money_change")
    var moneyExcess: Double? = 0.toDouble()

    @SerializedName("date_request_payment")
    var dateRequestPayment: String? = ""

    @SerializedName("date_add")
    var dateAdd: String? = ""

    @SerializedName("date_complete")
    var dateComplete: String? = ""

    @SerializedName("tmp_customer_name")
    var customerName: String? = ""

    @SerializedName("tmp_staff_name")
    var staffName: String? = ""

    @SerializedName("tmp_shift")
    var shift: String? = ""

    @SerializedName("is_staff_order")
    var isStaffOrder: Int? = 0

    //list table name split by regex ,
    @SerializedName("list_table_name")
    var listTableName: String? = ""

}