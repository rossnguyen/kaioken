package agv.kaak.vn.kaioken.entity.result

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class KiOfPageResult : Serializable {
    @SerializedName("user_id")
    var userId: Int = -1

    @SerializedName("page_id")
    var pageId: Int = -1

    @SerializedName("ki_point")
    var kiPoint: Double? = 0.toDouble()
}