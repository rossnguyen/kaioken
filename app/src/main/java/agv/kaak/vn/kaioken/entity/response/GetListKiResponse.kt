package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.result.KiOfUserResult
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetListKiResponse : BaseResponse(), Serializable {
    @SerializedName("result")
    var kiOfUser: KiOfUserResult? = null
}