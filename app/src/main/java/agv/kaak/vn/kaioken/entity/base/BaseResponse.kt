package agv.kaak.vn.kaioken.entity.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class BaseResponse:Serializable{
    @SerializedName("status")
    @Expose
    val status: Int? = null

    @SerializedName("error")
    @Expose
    val error: Error? = null
}