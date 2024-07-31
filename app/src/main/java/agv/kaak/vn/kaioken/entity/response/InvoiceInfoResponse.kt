package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.result.InvoiceInfo
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class InvoiceInfoResponse:BaseResponse(), Serializable {
    @SerializedName("result")
    var invoiceInfo: InvoiceInfo?=null
}