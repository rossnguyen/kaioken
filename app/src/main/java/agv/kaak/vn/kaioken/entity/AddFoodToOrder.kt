package agv.kaak.vn.kaioken.entity

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable

class AddFoodToOrder : Serializable {
    @SerializedName("order_id")
    var orderId: Int = 0

    @SerializedName("order_item")
    var listItem: ArrayList<DetailOrder> = arrayListOf()

    @SerializedName("reason")
    var reason: String = ""

    constructor(orderId: Int, listItem: ArrayList<DetailOrder>, reason: String) {
        this.orderId = orderId
        this.listItem = listItem
        this.reason = reason
    }

    constructor()

    fun newInstance(): AddFoodToOrder {
        return AddFoodToOrder(orderId, listItem, reason)
    }

    fun toJsonObject(): JSONObject? {
        var result: JSONObject? = null
        val gson = Gson()
        try {
            result = JSONObject(gson.toJson(newInstance()))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }

}