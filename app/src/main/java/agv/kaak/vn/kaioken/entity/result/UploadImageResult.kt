package agv.kaak.vn.kaioken.entity.result

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UploadImageResult:Serializable {
    @SerializedName("url")
    @Expose
    var url: String=""
}