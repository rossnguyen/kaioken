package agv.kaak.vn.kaioken.entity.response.wallet

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by shakutara on 28/12/2017.
 */
class CreateWalletResponse : BaseResponse() {

    @SerializedName("result")
    @Expose
    var itemWallet: ItemWallet? = null

}