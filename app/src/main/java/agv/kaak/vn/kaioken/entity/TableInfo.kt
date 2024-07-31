package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TableInfo :Serializable{

    @SerializedName("table_id")
    @Expose
    var tableId: Int? = -1
    @SerializedName("table_name")
    @Expose
    var tableName: String? = ""
    @SerializedName("type_id")
    @Expose
    var typeId: Int? = -1
    @SerializedName("is_active")
    @Expose
    var isActive: Int? = -1
    @SerializedName("order_id")
    @Expose
    var orderId: Int? = -1
    @SerializedName("table_status")
    @Expose
    var tableStatus: Int? = -1
    @SerializedName("price_after")
    @Expose
    var priceAfter: Double? = 0.toDouble()
    @SerializedName("total_item")
    @Expose
    var totalItem: Double? = 0.toDouble()
    @SerializedName("status_id")
    @Expose
    var statusId: Int? = -1
    @SerializedName("area_id")
    @Expose
    var areaId: Int? = -1
    @SerializedName("area_name")
    @Expose
    var areaName: String? = ""
}