package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Created by shakutara on 20/01/2018.
 */
class CustomerInfo : Serializable {

    @SerializedName("user_id")
    @Expose
    var userId: Int? = null
    @SerializedName("user_name")
    @Expose
    var userName: String? = null
    @SerializedName("avarta")
    @Expose
    var avarta: String? = null

    @SerializedName("email")
    var email: String? = ""

    @SerializedName("tmp_customer_name")
    var tmpCustomerName:String? = ""

}