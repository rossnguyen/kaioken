package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UpdateUserInfoResponse:BaseResponse(), Serializable {
    @SerializedName("result")
    var result:Boolean?=false
}