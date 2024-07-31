package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.StorePromotion
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DetailPromotionResponse:BaseResponse(), Serializable {
    @SerializedName("result")
    var detailPromotion: StorePromotion?=null
}