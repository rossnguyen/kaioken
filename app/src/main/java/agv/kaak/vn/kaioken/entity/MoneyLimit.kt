package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by shakutara on 28/12/2017.
 */
class MoneyLimit {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("user_id")
    @Expose
    var userId: String? = null
    @SerializedName("wallet_limit")
    @Expose
    var walletLimit: String? = null
    @SerializedName("month")
    @Expose
    var month: String? = null
    @SerializedName("date_add")
    @Expose
    var dateAdd: Any? = null
    @SerializedName("date_update")
    @Expose
    var dateUpdate: Any? = null

}