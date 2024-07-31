package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.result.DetailStoreResult
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DetailStoreResponse:BaseResponse(),Serializable {
    @SerializedName("result")
    var detailStoreResult:DetailStoreResult? = null
}