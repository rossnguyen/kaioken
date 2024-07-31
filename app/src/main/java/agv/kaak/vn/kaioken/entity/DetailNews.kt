package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DetailNews : Serializable {
    @SerializedName("news_id")
    var news_id: Int? = -1

    @SerializedName("news_title")
    var title: String? = ""

    @SerializedName("news_title_escape")
    var titleEscape: String? = ""

    @SerializedName("new_title_ascii")
    var titleAscii: String? = ""

    @SerializedName("new_slug")
    var slug: String? = ""

    @SerializedName("new_url_short")
    var urlShort: String? = ""

    @SerializedName("news_url")
    var url: String? = ""

    @SerializedName("news_description")
    var description: String? = ""

    @SerializedName("content")
    var content: String? = ""

    @SerializedName("thumbnail")
    val thumbnail: String? = ""

    @SerializedName("banner")
    var banner: String? = ""

    @SerializedName("type_id")
    var typeId: Int? = 0

    @SerializedName("source_id")
    var sourceId: Int? = -1

    @SerializedName("is_active")
    var isActive: Int? = 0

    @SerializedName("is_hot")
    var isHot: Int? = 0

    @SerializedName("is_expire")
    var isExpire: Int? = 0

    @SerializedName("number")
    var priority: Int? = 0

    @SerializedName("page_id")
    var pageId: Int? = -1

    @SerializedName("promotion_id")
    var promotionId: Int? = -1

    @SerializedName("news_tags")
    var tag: String? = ""

    @SerializedName("news_tag_ascii")
    var tagAscii: String? = ""

    @SerializedName("event_url")
    var eventUrl: String? = ""

    @SerializedName("date_start")
    var dateStart: String? = ""

    @SerializedName("date_end")
    var dateEnd: String? = ""

    @SerializedName("youtube_id")
    var youTubeId: String? = ""

}