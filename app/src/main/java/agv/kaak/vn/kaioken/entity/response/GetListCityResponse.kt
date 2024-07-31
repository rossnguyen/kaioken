package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.City
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetListCityResponse : BaseResponse(), Serializable {
    @SerializedName("result")
    var listCity: ArrayList<City>? = null
}