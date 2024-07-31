package agv.kaak.vn.kaioken.entity.result

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DetailRecruitmentResult:Serializable {
    @SerializedName("recruitment_id")
    var id: Int = 0

    @SerializedName("page_id")
    var pageId: Int = 0

    @SerializedName("image")
    var linkImage: String=""

    @SerializedName("number")
    var quantity: Int = 0

    @SerializedName("title")
    var title: String=""

    @SerializedName("description")
    var description: String=""

    @SerializedName("is_active")
    var isActive: Int = 0
}