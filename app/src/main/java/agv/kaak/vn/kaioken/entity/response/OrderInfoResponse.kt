package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.OrderInfo
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OrderInfoResponse : BaseResponse(), Serializable {
    @SerializedName("result")
    var orderInfo: OrderInfo? = null
}