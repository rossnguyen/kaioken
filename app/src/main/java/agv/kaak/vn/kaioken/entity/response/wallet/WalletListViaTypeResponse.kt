package agv.kaak.vn.kaioken.entity.response.wallet

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import agv.kaak.vn.kaioken.entity.response.wallet.ItemWallet


/**
 * Created by shakutara on 27/12/2017.
 */
class WalletListViaTypeResponse : BaseResponse() {

    @SerializedName("result")
    @Expose
    var itemWalletList: List<ItemWallet>? = null

}