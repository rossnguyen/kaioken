package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.result.CreateStoreCommentResult
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CreateStoreCommentResponse:BaseResponse(), Serializable {
    @SerializedName("result")
    var result: CreateStoreCommentResult?=null
}