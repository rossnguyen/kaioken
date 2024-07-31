package agv.kaak.vn.kaioken.entity.response.wallet

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Created by shakutara on 27/12/2017.
 */
class ItemWallet() : Serializable {

    constructor(walletType: String?, title: String?, amount: String?, date: String?) : this() {
        this.walletType = walletType
        this.title = title
        this.amount = amount
        this.date = date
    }

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("wallet_type")
    @Expose
    var walletType: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("amount")
    @Expose
    var amount: String? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("invoice_no")
    @Expose
    var invoiceNo: String? = null


}