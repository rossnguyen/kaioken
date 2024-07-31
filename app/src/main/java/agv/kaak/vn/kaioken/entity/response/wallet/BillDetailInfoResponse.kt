package agv.kaak.vn.kaioken.entity.response.wallet

import agv.kaak.vn.kaioken.entity.BillDetailInfo
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * Created by shakutara on 20/01/2018.
 */
class BillDetailInfoResponse : BaseResponse() {

    @SerializedName("result")
    @Expose
    var billDetailInfo: BillDetailInfo? = null

}