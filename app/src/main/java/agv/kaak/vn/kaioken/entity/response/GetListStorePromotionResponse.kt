package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.StorePromotion
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetListStorePromotionResponse:BaseResponse(), Serializable {
    @SerializedName("result")
    var promotions:ArrayList<StorePromotion> = arrayListOf()
}