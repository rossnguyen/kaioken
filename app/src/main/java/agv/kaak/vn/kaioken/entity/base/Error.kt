package agv.kaak.vn.kaioken.entity.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Error:Serializable {
    @SerializedName("code")
    @Expose
    val code: Int? = null
    @SerializedName("msg")
    @Expose
    val msg: String? = null
    @SerializedName("desc")
    @Expose
    val desc: String? = null
}