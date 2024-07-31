package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.result.UserInfo
import com.google.gson.annotations.SerializedName

class CheckPhoneExistResponse : BaseResponse() {
    @SerializedName("result")
    var userInfo: UserInfo? = null
}