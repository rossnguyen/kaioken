package agv.kaak.vn.kaioken.entity.result

import agv.kaak.vn.kaioken.entity.response.KiInfo
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class KiOfUserResult : Serializable {
    @SerializedName("user_id")
    var userId: Int? = -1

    @SerializedName("name")
    var name: String? = ""

    @SerializedName("avarta")
    var linkAvatar: String? = ""

    @SerializedName("qrcode")
    var qrCode: String? = ""

    @SerializedName("cover")
    var cover: String? = ""

    @SerializedName("cover_small")
    var coverSmall: String? = ""

    @SerializedName("phone")
    var phone:String?=""

    @SerializedName("total_ki")
    var totalKi: Double? = 0.toDouble()

    @SerializedName("list_ki")
    var listKi: ArrayList<KiInfo> = arrayListOf()
}