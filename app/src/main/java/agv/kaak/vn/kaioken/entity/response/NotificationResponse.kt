package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.result.NotificationResult
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class NotificationResponse : BaseResponse(), Serializable {
    @SerializedName("result")
    var listNoti: ArrayList<NotificationResult>? = null
}