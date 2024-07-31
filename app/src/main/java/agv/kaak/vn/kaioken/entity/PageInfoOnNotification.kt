package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PageInfoOnNotification:Serializable {
    @SerializedName("page_id")
    var pageId: Int? = 0

    @SerializedName("page_name")
    var pageName: String?=""

    @SerializedName("image")
    var linkImage: String?=""
}