package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.result.DetailCoupon
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DetailCouponResponse : BaseResponse(), Serializable {
    @SerializedName("result")
    var detailCoupon: DetailCoupon? = null
}