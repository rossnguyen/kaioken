package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class InfoItemNotification:Serializable {
    @SerializedName("page_info")
    var pageInfo: PageInfoOnNotification?=null

    @SerializedName("sender_info")
    var senderInfo: SenderInfoOnNotification?=null

    @SerializedName("handler_info")
    var handlerInfo: SenderInfoOnNotification?=null
}