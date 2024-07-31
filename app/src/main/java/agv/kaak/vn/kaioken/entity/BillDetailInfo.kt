package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Created by shakutara on 20/01/2018.
 */
class BillDetailInfo : Serializable {

    @SerializedName("list_room")
    @Expose
    var listRoom: List<Room>? = null
    @SerializedName("list_item")
    @Expose
    var listItem: List<OrderDetail>? = null
    @SerializedName("bill_info")
    @Expose
    var billInfo: OrderDetail? = null
    @SerializedName("customer_info")
    @Expose
    var customerInfo: CustomerInfo? = null
    @SerializedName("cashier_info")
    @Expose
    var cashierInfo: CashierInfo? = null
    @SerializedName("table_info")
    @Expose
    var tableInfo: Table? = null
    @SerializedName("page_info")
    @Expose
    var pageInfo: PageInfo? = null

}