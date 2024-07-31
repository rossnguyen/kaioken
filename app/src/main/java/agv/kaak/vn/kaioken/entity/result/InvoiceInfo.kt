package agv.kaak.vn.kaioken.entity.result

import agv.kaak.vn.kaioken.entity.*
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class InvoiceInfo : Serializable {
    @SerializedName("bill_info")
    var invoiceInfo: DetailOrderInfo? = null

    @SerializedName("list_item")
    var listItem: ArrayList<DetailOrder>? = null

    @SerializedName("customer_info")
    var customerInfo: CustomerInfo? = null

    @SerializedName("cashier_info")
    var cashierInfo: CashierInfo? = null

    @SerializedName("table_info")
    var tableInfo: TableInfo? = null

    @SerializedName("page_info")
    var pageInfo: PageInfo? = null

    @SerializedName("total_order")
    var totalOrder: Int? = 0

    fun getTotalItem():Int{
        var result=0
        listItem?.forEach {
            result+=it.numberOrder!!
        }

        return result
    }
}