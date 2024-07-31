package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.result.TrackOrder
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TrackOrderResponse : BaseResponse(), Serializable {
    @SerializedName("result")
    var listOrdering: ArrayList<TrackOrder>? = null
}