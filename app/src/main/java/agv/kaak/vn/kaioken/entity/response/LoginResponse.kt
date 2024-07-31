package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.result.LoginResult
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LoginResponse:BaseResponse(),Serializable {
    @SerializedName("result")
    var loginResult:LoginResult?=null
}