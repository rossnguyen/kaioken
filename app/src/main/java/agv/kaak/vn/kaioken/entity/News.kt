package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class News : Serializable {
    @SerializedName("news_id")
    var news_id: Int? = -1

    @SerializedName("news_title")
    var title: String? = ""

    @SerializedName("news_url")
    var url: String? = ""

    @SerializedName("news_description")
    var description: String? = ""

    @SerializedName("thumbnail")
    val thumbnail: String? = ""

    @SerializedName("banner")
    var banner: String? = ""

    @SerializedName("is_hot")
    var isHot: Int? = 0
}