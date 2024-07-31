package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ImageDetailItemFood : Serializable {
    @SerializedName("image_id")
    var imageId: Int = 0

    @SerializedName("size1")
    var url: String=""
}