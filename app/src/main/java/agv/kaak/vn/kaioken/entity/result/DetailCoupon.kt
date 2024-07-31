package agv.kaak.vn.kaioken.entity.result

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class DetailCoupon :Serializable {
    @SerializedName("campaign_id")
    var campaignId:Int?=-1

    @SerializedName("page_id")
    var pageId:Int?=-1

    @SerializedName("name")
    var name:String?=""

    @SerializedName("description")
    var description:String?=""

    @SerializedName("type_id")
    var typeId:Int?=-1


    @SerializedName("discount_type")
    var discountType:Int?=-1

    @SerializedName("discount")
    var discountValue:Int?=-1

    @SerializedName("total_coupon")
    var totalCoupon:Int?=-1

    @SerializedName("total_per_coupon")
    var totalPerCoupon:Int?=-1

    @SerializedName("is_active")
    var isActive:Int?= -1

    @SerializedName("date_start")
    var dateStart: String?=""

    @SerializedName("date_end")
    var dateEnd:String?=""

    @SerializedName("date_add")
    var dateAdd:String?=""

    @SerializedName("coupon_id")
    var couponId:Int?=-1

    @SerializedName("coupon_campaign_id")
    var couponCampaignId:Int?=-1

    @SerializedName("coupon_code")
    var couponCode:String?=""

    @SerializedName("coupon_qr")
    var couponQR:String?=""

    @SerializedName("status_id")
    var statusId:Int?=-1

    @SerializedName("device_id")
    var deviceId:Int?=-1

    @SerializedName("customer_id")
    var customerId:Int?=-1

    @SerializedName("order_id")
    var orderId:Int?=-1

    @SerializedName("total_per_used")
    var totalPerUsed:Int?=-1

    @SerializedName("date_send")
    var dateSend:String?=""

    @SerializedName("date_use")
    var dateUse:String?=""

    @SerializedName("date_update")
    var dateUpdate:String?=""
}