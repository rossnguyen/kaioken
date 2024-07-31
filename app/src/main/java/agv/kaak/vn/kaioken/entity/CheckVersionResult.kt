package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CheckVersionResult : Serializable {
    @SerializedName("app_version")
    var appVersion: String? = ""

    @SerializedName("type_id")
    var typeId: Int? = -1

    @SerializedName("title")
    var title: String? = ""

    @SerializedName("new_feature")
    var newFeatures: String? = ""

    @SerializedName("new_permission")
    var newPermission: String? = ""

    @SerializedName("is_active")
    var isActive: Int? = 0

    @SerializedName("date_add")
    var dateAdd: String? = ""

    @SerializedName("date_update")
    var dateUpdate: String? = ""

    @SerializedName("is_required")
    var isRequired: Int? = 0

}