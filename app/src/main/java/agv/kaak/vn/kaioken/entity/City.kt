package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class City: Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("latitude")
    @Expose
    var latitude: String? = null
    @SerializedName("longitude")
    @Expose
    var longitude: String? = null
}