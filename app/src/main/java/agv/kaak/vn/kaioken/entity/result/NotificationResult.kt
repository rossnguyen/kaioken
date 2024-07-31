package agv.kaak.vn.kaioken.entity.result

import agv.kaak.vn.kaioken.entity.InfoItemNotification
import agv.kaak.vn.kaioken.entity.SubNoti
import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class NotificationResult : Serializable {
    @SerializedName("_id")
    var id: Int = -1

    /*@SerializedName("notify_id")
    var notifyId = -1*/

    @SerializedName("page_id")
    var pageId: Int = -1

    @SerializedName("order_id")
    var orderId: Int = -1

    @SerializedName("general_key")
    var generalKey: Int? = -1

    @SerializedName("message_type")
    var messageType: Int = -1

    @SerializedName("creator_id")
    var userId: Int = -1

    @SerializedName("creator_department_id")
    var tabId: Int = -1

    @SerializedName("status_id")
    var statusId: Int = -1

    @SerializedName("is_rating")
    var isRating: Int = 0

    @SerializedName("content")
    var content: String = ""

    @SerializedName("date_created")
    var dateCreated: String? = null

    @SerializedName("info")
    var info: InfoItemNotification? = null

    @SerializedName("count")
    var subCount: Int? = 0

    @SerializedName("sub")
    var sub: ArrayList<Int>? = null

    var listSub: ArrayList<SubNoti>? = null

    var isExpand: Boolean? = false

    var seen: Boolean? = false

    var imageBitmap: Bitmap? = null

    var isChecked=false
}