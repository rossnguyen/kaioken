package agv.kaak.vn.kaioken.entity

import agv.kaak.vn.kaioken.entity.result.UserInfo
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OrderInfo : Serializable {
    @SerializedName("list_item")
    var listItem: List<DetailOrder>? = null

    @SerializedName("order_detail")
    var detailOrderInfo: DetailOrderInfo? = null

    @SerializedName("user_info")
    var userInfo: UserInfo? = null

    @SerializedName("staff_confirm")
    var staffInfo: CashierInfo? = null

    @SerializedName("cashier_confirm")
    var cashierInfo: CashierInfo? = null

    @SerializedName("chef_confirm")
    var chefInfo: CashierInfo? = null

    @SerializedName("table_info")
    var tableInfo: TableInfo? = null
}