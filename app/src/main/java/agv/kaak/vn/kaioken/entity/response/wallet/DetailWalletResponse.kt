package agv.kaak.vn.kaioken.entity.response.wallet

import agv.kaak.vn.kaioken.entity.DetailWallet
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by shakutara on 23/01/2018.
 */
class DetailWalletResponse: BaseResponse() {

    @SerializedName("result")
    @Expose
    var detailWalletList: List<DetailWallet>? = null

}