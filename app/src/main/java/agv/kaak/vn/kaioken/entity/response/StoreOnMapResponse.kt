package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.Store
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StoreOnMapResponse:BaseResponse(), Serializable {
    @SerializedName("result")
    var listStore:ArrayList<Store>? = null
}