package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.result.UserInfo
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserResponse : BaseResponse(), Serializable {
    @SerializedName("result")
    var user: UserInfo? = null
}