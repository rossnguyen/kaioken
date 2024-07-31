package agv.kaak.vn.kaioken.entity.result

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ListRatedResult :BaseResponse(),Serializable {
    @SerializedName("list_page_id")
    var listPageId:String?=""
}