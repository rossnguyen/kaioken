package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SenderInfoOnNotification:Serializable {
    @SerializedName("user_id")
    var userId:Int?=-1

    @SerializedName("user_name")
    var userName:String?=""

    @SerializedName("avarta")
    var linkAvatar:String?=""

    @SerializedName("department_name")
    var departmentName:String?=""
}