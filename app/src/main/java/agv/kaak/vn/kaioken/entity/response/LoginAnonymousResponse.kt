package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.result.LoginAnonymousResult
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LoginAnonymousResponse:BaseResponse(),Serializable {
    @SerializedName("result")
    var result:LoginAnonymousResult? = null
}