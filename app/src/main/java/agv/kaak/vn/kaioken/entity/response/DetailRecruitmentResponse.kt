package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.result.DetailRecruitmentResult
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DetailRecruitmentResponse:BaseResponse(), Serializable {
    @SerializedName("result")
    var result: DetailRecruitmentResult?=null
}