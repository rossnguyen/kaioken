package agv.kaak.vn.kaioken.entity.result

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CheckTokenValidResult:Serializable {
    @SerializedName("device_name")
    @Expose
    val deviceName: String? = null
    @SerializedName("user_agent")
    @Expose
    val userAgent: String? = null
    @SerializedName("ip_address")
    @Expose
    val ipAddress: String? = null
}