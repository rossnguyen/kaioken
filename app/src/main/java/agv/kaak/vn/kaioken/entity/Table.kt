package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Created by shakutara on 08/01/2018.
 */
class Table : Serializable {

    @SerializedName("table_id")
    @Expose
    var tableId: Int? = null
    @SerializedName("table_name")
    @Expose
    var tableName: String? = null
    @SerializedName("type_id")
    @Expose
    var typeId: Int? = null
    @SerializedName("is_active")
    @Expose
    var isActive: Int? = null
    @SerializedName("order_id")
    @Expose
    var orderId: Int? = null
    @SerializedName("table_status")
    @Expose
    var tableStatus: Int? = null
    @SerializedName("price_after")
    @Expose
    var priceAfter: Double? = null
    @SerializedName("total_item")
    @Expose
    var totalItem: Double? = null
    @SerializedName("status_id")
    @Expose
    var statusId: Int? = null
    @SerializedName("area_id")
    @Expose
    var areaId: Int? = null
    @SerializedName("area_name")
    @Expose
    var areaName: String? = null

}