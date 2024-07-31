package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.result.KiOfPageResult
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetKiOfPageResponse : BaseResponse(), Serializable {
    @SerializedName("result")
    var kiOfPage: KiOfPageResult? = null
}