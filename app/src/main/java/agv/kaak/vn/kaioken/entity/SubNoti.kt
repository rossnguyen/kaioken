package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SubNoti:Serializable {
    @SerializedName("_id")
    var id: Int = 0
    @SerializedName("content")
    var content: String?=""
}