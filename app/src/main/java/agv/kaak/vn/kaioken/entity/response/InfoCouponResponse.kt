package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.result.InfoCoupon
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class InfoCouponResponse : BaseResponse(), Serializable {
    @SerializedName("result")
    var infoCoupon: InfoCoupon? = null
}