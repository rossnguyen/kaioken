package agv.kaak.vn.kaioken.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import agv.kaak.vn.kaioken.entity.response.wallet.ItemWallet


/**
 * Created by shakutara on 27/12/2017.
 */
class DetailWallet {

    @SerializedName("total_thu")
    @Expose
    var totalThu: String? = null
    @SerializedName("total_chi")
    @Expose
    var totalChi: String? = null
    @SerializedName("total_muon")
    @Expose
    var totalMuon: String? = null
    @SerializedName("total_chomuon")
    @Expose
    var totalChomuon: String? = null
    @SerializedName("dinh_muc")
    @Expose
    var dinhMuc: Int? = null
    @SerializedName("detail")
    @Expose
    var detail: List<ItemWallet>? = null
    @SerializedName("month")
    @Expose
    var month: Int? = null

}