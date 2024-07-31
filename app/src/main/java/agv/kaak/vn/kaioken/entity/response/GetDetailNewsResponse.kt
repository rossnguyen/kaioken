package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.DetailNews
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetDetailNewsResponse : BaseResponse(), Serializable {
    @SerializedName("result")
    var detailNews: DetailNews? = null

}