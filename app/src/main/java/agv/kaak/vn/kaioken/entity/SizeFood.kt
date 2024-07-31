package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SizeFood : Serializable {
    @SerializedName("id")
    var id: Int? = -1

    @SerializedName("size_type")
    var sizeType: Int = 0

    @SerializedName("size_name")
    var sizeName: String = ""

    @SerializedName("is_active")
    var isActive: Int = 0

    @SerializedName("price")
    var price: Double = 0.toDouble()

    var priceAfterPromotion: Double = 0.toDouble()
}