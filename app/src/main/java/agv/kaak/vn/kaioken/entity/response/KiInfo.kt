package agv.kaak.vn.kaioken.entity.response

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class KiInfo : Serializable {
    @SerializedName("id")
    var id: Int? = -1

    @SerializedName("ki_point")
    var kiPoint: Double? = 0.toDouble()

    @SerializedName("page_id")
    var pageId: Int? = 0

    @SerializedName("page_name")
    var pageName: String? = ""

    @SerializedName("image")
    var linkPageImage: String? = ""

    var imageBitmap: Bitmap? = null
}