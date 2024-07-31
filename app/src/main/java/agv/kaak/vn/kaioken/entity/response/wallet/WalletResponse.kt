package agv.kaak.vn.kaioken.entity.response.wallet

import agv.kaak.vn.kaioken.entity.DetailWallet
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by shakutara on 27/12/2017.
 */
class WalletResponse : BaseResponse() {

    @SerializedName("result")
    @Expose
    var detail: DetailWallet? = null

}