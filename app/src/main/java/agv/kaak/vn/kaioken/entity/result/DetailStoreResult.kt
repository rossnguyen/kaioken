package agv.kaak.vn.kaioken.entity.result

import agv.kaak.vn.kaioken.entity.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DetailStoreResult : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int = 0

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("description")
    var description: String? = ""

    @SerializedName("working_hour")
    @Expose
    var workingHour: String? = null

    @SerializedName("is_using_app")
    @Expose
    var isUsingApp: Int? = null

    @SerializedName("is_shipping")
    @Expose
    var isShipping: Int? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("cover")
    @Expose
    var cover: String? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = ""

    @SerializedName("rating")
    @Expose
    var rating: String? = ""

    @SerializedName("order_form")
    @Expose
    var orderForm: ArrayList<OrderForm> = arrayListOf()

    @SerializedName("count_image")
    @Expose
    var countImage: Int? = 0

    @SerializedName("count_comment")
    @Expose
    var countComment: Int? = 0

    @SerializedName("count_recuitment")
    @Expose
    var countRecuitment: Int? = 0

    @SerializedName("status")
    @Expose
    var isOpening: Int? = 0

    @SerializedName("table_available")
    @Expose
    var tableAvailable: Int? = 0

    @SerializedName("page_follow")
    @Expose
    var pageFollow: Int? = 0

    @SerializedName("promotion")
    var promotion: StorePromotion? = null

    @SerializedName("menuInfo")
    @Expose
    var menuInfo: ArrayList<MenuInfo>? = arrayListOf()

    @SerializedName("roomInfo")
    @Expose
    var menuRoom: ArrayList<MenuInfo>? = null

    @SerializedName("location")
    @Expose
    var location: SerializableLatLng? = null

    @SerializedName("contact")
    @Expose
    var contact: ContactStore? = null

    fun isAllowOrderLocal(): Boolean {
        orderForm.forEach {
            if (it.alias == "local")
                return true
        }

        return false
    }
}