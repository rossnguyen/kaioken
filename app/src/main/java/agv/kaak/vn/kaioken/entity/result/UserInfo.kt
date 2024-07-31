package agv.kaak.vn.kaioken.entity.result

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class UserInfo : Serializable {
    @SerializedName("id")
    var id: Int? = 0

    @SerializedName("name")
    var name: String? = ""

    @SerializedName("avarta")
    var linkAvarta: String? = ""

    @SerializedName("cover_small")
    var linkCoverSmall: String? = ""

    @SerializedName("cover_large")
    var linkCoverLarge: String? = ""

    @SerializedName("gender")
    var gender: Int? = 0

    @SerializedName("birth_date")
    var birthDay: Date? = null

    @SerializedName("phone")
    var phone: String? = ""

    @SerializedName("email")
    var email: String? = ""
}