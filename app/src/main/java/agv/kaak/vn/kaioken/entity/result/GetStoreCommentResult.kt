package agv.kaak.vn.kaioken.entity.result

import agv.kaak.vn.kaioken.entity.ImageComment
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetStoreCommentResult:Serializable {
    @SerializedName("user_id")
    @Expose
    var userId: String=""

    @SerializedName("user_avarta")
    @Expose
    var userAvatar: String?=""

    @SerializedName("page_id")
    @Expose
    var pageId: Int=-1

    @SerializedName("user_name")
    @Expose
    var userName: String=""

    @SerializedName("content")
    @Expose
    var content: String=""

    @SerializedName("date_add")
    @Expose
    var dateAdd: String=""

    @SerializedName("list_image")
    @Expose
    var listImage: ArrayList<ImageComment> = arrayListOf()
}