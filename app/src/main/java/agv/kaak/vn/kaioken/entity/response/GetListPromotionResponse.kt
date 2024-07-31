package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.Promotion
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetListPromotionResponse : BaseResponse(), Serializable {
    @SerializedName("result")
    var listPromotion: ArrayList<Promotion>? = null
}