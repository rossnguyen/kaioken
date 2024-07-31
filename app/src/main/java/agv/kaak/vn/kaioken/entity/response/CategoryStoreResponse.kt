package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.result.CategoryStoreResult
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.ArrayList

class CategoryStoreResponse:BaseResponse(), Serializable {
    @SerializedName("result")
    @Expose
    var data: ArrayList<CategoryStoreResult>? = arrayListOf()
}