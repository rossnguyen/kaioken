package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ToppingForSocket : Serializable {
    @SerializedName("item_id")
    var itemId: Int? = -1

    @SerializedName("name")
    var nameTopping: String = ""

    @SerializedName("price_after")
    var priceAfter: Double? = 0.0

    constructor(itemId: Int?, nameTopping: String, priceAfter: Double?) {
        this.itemId = itemId
        this.nameTopping = nameTopping
        this.priceAfter = priceAfter
    }
}