package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.result.CheckTokenValidResult
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CheckTokenValidResponse:BaseResponse(),Serializable {
    @SerializedName("result")
    @Expose
    var result: CheckTokenValidResult? = null
}