package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class Room {

    @SerializedName("room_id")
    @Expose
    var roomId: Int? = null
    @SerializedName("room_name")
    @Expose
    var roomName: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("type_id")
    @Expose
    var typeId: Int? = null
    @SerializedName("is_active")
    @Expose
    var isActive: Int? = null
    @SerializedName("order_id")
    @Expose
    var orderId: Int? = null
    @SerializedName("room_status")
    @Expose
    var roomStatus: Int? = null
    @SerializedName("price_before")
    @Expose
    var priceBefore: Double? = null
    @SerializedName("price_after")
    @Expose
    var priceAfter: Double? = null
    @SerializedName("total_item")
    @Expose
    var totalItem: Double? = null
    @SerializedName("status_id")
    @Expose
    var statusId: Int? = null

}