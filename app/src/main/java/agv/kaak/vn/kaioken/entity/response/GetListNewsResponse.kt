package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.News
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetListNewsResponse:BaseResponse(), Serializable {
    @SerializedName("result")
    var result:ArrayList<News>? = null
}