package agv.kaak.vn.kaioken.entity.response

import agv.kaak.vn.kaioken.entity.OrderForm
import agv.kaak.vn.kaioken.entity.base.BaseResponse
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetListOrderFormPaidResponse : BaseResponse(), Serializable {
    @SerializedName("result")
    val listOrderFormPaid: ArrayList<OrderForm> = arrayListOf()
}