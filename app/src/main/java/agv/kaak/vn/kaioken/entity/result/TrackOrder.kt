package agv.kaak.vn.kaioken.entity.result

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TrackOrder : Serializable {
    @SerializedName("order_id")
    var orderId: Int? = -1

    @SerializedName("invoice_no")
    var invoiceNo: String? = ""

    @SerializedName("status_id")
    var statusId: Int? = -1

    @SerializedName("order_type")
    var orderType: Int? = -1

    @SerializedName("price_after")
    var priceAfter: Double? = 0.toDouble()

    @SerializedName("page_id")
    var pageId: Int? = -1

    @SerializedName("page_name")
    var pageName: String? = ""

    @SerializedName("address")
    var pageAddress: String? = ""

    @SerializedName("image")
    var imageStore: String? = ""

    @SerializedName("phone")
    var phone: String? = ""

    @SerializedName("delivery_time_arrive")
    var deliveryTimeArrive: String? = ""

    @SerializedName("order_table_time_arrive")
    var orderTableTimeArrive: String? = ""

    @SerializedName("date_complete")
    var dateComplete: String? = ""

    @SerializedName("is_rate")
    var isRate: Int? = 0

    var imageBitmap: Bitmap? = null

    var isChecked = false
}