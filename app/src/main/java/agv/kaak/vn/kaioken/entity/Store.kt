package agv.kaak.vn.kaioken.entity

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Store : Serializable {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String? = ""

    @SerializedName("address")
    var address: String? = ""

    @SerializedName("is_using_app")
    var isUsingApp: Int = 0

    @SerializedName("is_shipping")
    var isShipping: Int = 0

    @SerializedName("rating")
    var rating: Float? = null

    @SerializedName("discount")
    var discount: String? = ""

    @SerializedName("discount_type")
    var discountType: Int? = 1

    @SerializedName("reason")
    var reason: String? = ""

    @SerializedName("is_follow")
    var liked: Int = 0

    @SerializedName("image")
    var image: String? = null

    @SerializedName("cover_small")
    var coverSmall: String? = null


    @SerializedName("distance")
    var distance: Double? = null

    @SerializedName("location")
    var location: LatLng? = null

    @SerializedName("promotion_id")
    var promotionId: Int? = -1

    var imageBitMap: Bitmap? = null
}