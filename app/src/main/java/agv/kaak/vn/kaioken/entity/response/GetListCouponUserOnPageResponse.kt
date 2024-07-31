package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.result.Coupon
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetListCouponUserOnPageResponse : BaseResponse(), Serializable {
    @SerializedName("result")
    var listCoupon: ArrayList<Coupon> = arrayListOf()
}