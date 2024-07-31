package agv.kaak.vn.kaioken.entity.response

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable

class SocketRealtimeSurchangeDiscountVat : Serializable {
    @SerializedName("status_id")
    var statusId: Int? = -1

    @SerializedName("price_after")
    var priceAfter: Double? = 0.toDouble()

    @SerializedName("price_before")
    var priceBefore: Double? = 0.toDouble()

    @SerializedName("discount_name")
    var discountName: String? = ""

    @SerializedName("discount_type")
    var discountType: Int? = 0

    @SerializedName("discount_value")
    var discountValue: Double? = 0.toDouble()

    @SerializedName("extra_name")
    var extraName: String? = ""

    @SerializedName("extra_value")
    var extraValue: Double? = 0.toDouble()

    @SerializedName("vat")
    var vat: Int? = 0


    constructor()

    fun newInstance(): SocketRealtimeSurchangeDiscountVat {
        val result = SocketRealtimeSurchangeDiscountVat()
        result.statusId = this.statusId
        result.priceAfter = this.priceAfter
        result.priceBefore = this.priceBefore
        result.discountName = this.discountName
        result.discountType = this.discountType
        result.discountValue = this.discountValue
        result.extraName = this.extraName
        result.extraValue = this.extraValue
        result.vat = this.vat

        return result
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

    companion object {
        fun fromJsonObject(jsonObject: JSONObject): SocketRealtimeSurchangeDiscountVat {
            return Gson().fromJson(jsonObject.toString(), SocketRealtimeSurchangeDiscountVat::class.java)
        }
    }
}