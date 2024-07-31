package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StorePromotion : Serializable {
    @SerializedName("promotion_id")
    var promotionId: Int? = -1

    @SerializedName("page_id")
    var pageId: Int? = -1

    @SerializedName("type_id")
    var typeId: Int? = 0

    @SerializedName("coupon_type")
    var couponType: Int? = 0

    @SerializedName("description")
    var description: String? = ""

    @SerializedName("discount_type")
    var discountType: Int? = 1

    @SerializedName("discount")
    var discountValue: Int? = 0

    @SerializedName("is_active")
    var isActive: Int? = 0

    @SerializedName("price_min")
    var priceMin: Double? = 0.toDouble()

    @SerializedName("price_max")
    var priceMax: Double? = 0.toDouble()

    @SerializedName("value_max")
    var valueMax: Double? = 0.toDouble()

    @SerializedName("reason")
    var reason: String? = ""

    @SerializedName("is_auto")
    var isAuto: Int? = 0

    @SerializedName("date_start")
    var dateStart: String? = ""

    @SerializedName("date_end")
    var dateEnd: String? = ""

    @SerializedName("date_add")
    var dateAdd: String? = ""

    @SerializedName("date_update")
    var dateUpdate: String? = ""

    @SerializedName("list_menu_category")
    var listMenuCategory: ArrayList<MenuCategoryPromotion>? = null

    @SerializedName("list_menu_item")
    var listMenuItem: ArrayList<MenuFood>? = null
}