package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OrderForm : Serializable {
    @SerializedName("id")
    var id: Int = -1

    @SerializedName("name")
    var name: String = ""

    @SerializedName("name_slug")
    var nameSlug: String = ""

    @SerializedName("alias")
    var alias: String = ""

    @SerializedName("icon")
    var icon: String? = null

    @SerializedName("is_active")
    var isActive: Int = 0

    @SerializedName("date_add")
    var dateAdd: String? = ""

    @SerializedName("date_update")
    var dateUpdate: String? = ""

    @SerializedName("payment_status")
    var paymentStatus: Int? = 0
}