package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ImageComment :Serializable {
    @SerializedName("image_id")
    @Expose
    var imageId: String=""

    @SerializedName("url")
    @Expose
    var url: String=""
}