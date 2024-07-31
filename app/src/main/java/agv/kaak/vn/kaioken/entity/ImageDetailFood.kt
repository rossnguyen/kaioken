package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ImageDetailFood : Serializable {
    @SerializedName("menu_item_id")
    var id: Int = 0

    @SerializedName("name")
    var name: String=""

    @SerializedName("price_min")
    var priceMin: Int = 0

    @SerializedName("list_image")
    var listImage: List<ImageDetailItemFood> = arrayListOf()
}