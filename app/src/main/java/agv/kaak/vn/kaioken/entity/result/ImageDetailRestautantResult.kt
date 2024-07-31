package agv.kaak.vn.kaioken.entity.result

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ImageDetailRestautantResult : Serializable {
    @SerializedName("image_id")
    var imageId: Int = 0

    @SerializedName("url")
    var url: String? = ""

}