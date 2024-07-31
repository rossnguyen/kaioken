package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.result.InfoScanToGoStoreResult
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ScanQRToGoStoreResponse : BaseResponse() {
    @SerializedName("result")
    @Expose
    var resultScan: InfoScanToGoStoreResult? = null
}