package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import com.google.gson.annotations.SerializedName

class RegisterAccountResponse : BaseResponse() {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("email")
    var email: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("sid")
    var sid: String? = null
}