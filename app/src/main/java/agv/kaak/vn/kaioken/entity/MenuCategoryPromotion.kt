package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MenuCategoryPromotion : Serializable {
    @SerializedName("id")
    var id: Int = -1

    @SerializedName("menu_cat_id")
    var menuCategoryId: Int = -1

    @SerializedName("name")
    var name: String = ""
}