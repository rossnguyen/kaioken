package agv.kaak.vn.kaioken.entity.result

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class InfoCoupon:Serializable {
    @SerializedName("promotion_id")
    var promotionId: Int? = -1

    @SerializedName("page_id")
    var pageId: Int? = -1

    @SerializedName("page_promotion_stt")
    var pagePromotionStt: Int? = -1

    @SerializedName("type_id")
    var typeId: Int? = -1

    @SerializedName("coupon_type")
    var couponType: Int? = 0

    @SerializedName("total_coupon")
    var totalCoupon: Int? = 0

    @SerializedName("location_id")
    var locationId: Int? = -1

    @SerializedName("description")
    var description: String? = ""

    @SerializedName("discount_type")
    var discountType: Int? = 1

    @SerializedName("discount")
    var discountValue: Int? = 0

    @SerializedName("is_active")
    var isActive: Int? = 0

    @SerializedName("reason")
    var reason: String? = ""

    @SerializedName("date_start")
    var dateStart: String? = ""

    @SerializedName("date_end")
    var dateEnd: String? = ""

    @SerializedName("date_add")
    var dateAdd: String? = ""

    @SerializedName("date_update")
    var dateUpdate: String? = ""

    @SerializedName("coupon_id")
    var couponId: Int? = -1

    @SerializedName("status_id")
    var statusId: Int? = 1

    @SerializedName("customer_id")
    var customerId: Int? = -1

    @SerializedName("order_id")
    var orderId: Int? = -1

    @SerializedName("notify_id")
    var notifyId: Int? = -1

    @SerializedName("date_send")
    var dateSend: String? = ""
}