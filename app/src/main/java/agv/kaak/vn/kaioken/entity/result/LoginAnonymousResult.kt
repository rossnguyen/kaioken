package agv.kaak.vn.kaioken.entity.result

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LoginAnonymousResult:Serializable {
    @SerializedName("id")
    var id:Int=0

    @SerializedName("email")
    var email:String?=""

    @SerializedName("name")
    var name:String?=""

    @SerializedName("sid")
    var sid:String?=""

}