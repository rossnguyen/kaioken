package agv.kaak.vn.kaioken.entity.result

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CategoryStoreResult:Serializable {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("name")
    var name: String? = ""
    @SerializedName("icon")
    var icon: String? = ""
}