package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Created by shakutara on 20/01/2018.
 */
class PageInfo : Serializable {

    @SerializedName("page_id")
    @Expose
    var pageId: Int? = null
    @SerializedName("page_name")
    @Expose
    var pageName: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("rating")
    var rating: Float? = 0.toFloat()

    @SerializedName("latitude")
    var latitude: Double? = 0.toDouble()

    @SerializedName("longitude")
    var longitude: Double? = 0.toDouble()

    @SerializedName("phone")
    var phone: String? = ""
}