package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ContactStore : Serializable {
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
}