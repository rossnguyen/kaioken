package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.ImageDetailFood
import agv.kaak.vn.kaioken.entity.MenuFood
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MenuItemDetail : BaseResponse(), Serializable {
    @SerializedName("result")
    var result: MenuFood? = null
}