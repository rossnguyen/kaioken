package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.result.ListRatedResult
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ListRatedResponse : BaseResponse(), Serializable {
    @SerializedName("result")
    var result: ListRatedResult? = null
}