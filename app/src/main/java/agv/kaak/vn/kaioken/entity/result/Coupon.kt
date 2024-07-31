package agv.kaak.vn.kaioken.entity.result

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Coupon : Serializable {
    @SerializedName("page_id")
    var pageId: Int = -1

    @SerializedName("page_name")
    var pageName: String? = ""

    @SerializedName("discount")
    var discount: Int = 0

    @SerializedName("discount_type")
    var discountType: Int = 0

    @SerializedName("reason")
    var reason: String? = ""

    @SerializedName("description")
    var description: String = ""

    @SerializedName("date_start")
    var dateStart: String? = ""

    @SerializedName("date_end")
    var dateEnd: String? = ""

    @SerializedName("coupon_id")
    var couponId: Int = -1

    @SerializedName("coupon_code")
    var couponCode: String = ""

    @SerializedName("order_id")
    var orderId: Int? = null

    @SerializedName("status_id")
    var statusId: Int = -1

    constructor(discount: Int, discountType: Int, couponCode: String) {
        this.discount = discount
        this.discountType = discountType
        this.couponCode = couponCode
    }
}