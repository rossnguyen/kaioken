package agv.kaak.vn.kaioken.entity

import agv.kaak.vn.kaioken.entity.define.DiscountType
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MenuFood : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int = -1
        private set

    @SerializedName("menu_item_id")
    var menuItemId: Int = -1

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("price")
    @Expose
    var price: Double = 0.toDouble()
        private set

    @SerializedName("price_min")
    @Expose
    var priceMin: Double = 0.toDouble()
        private set

    @SerializedName("price_max")
    @Expose
    var priceMax: Double = 0.toDouble()
        private set

    @SerializedName("image")
    var image: String? = ""

    @SerializedName("list_image")
    var listImage: List<ImageDetailItemFood> = arrayListOf()

    @SerializedName("description")
    var description: String? = null

    @SerializedName("parent_id")
    var parentId: Int? = -1

    @SerializedName("has_topping")
    var hasTopping: Int? = 0

    @SerializedName("is_promotion")
    var isPromotion: Int? = 0

    @SerializedName("list_size")
    var listSize: ArrayList<SizeFood> = arrayListOf()

    @SerializedName("list_topping_id")
    var listToppingId: ArrayList<Int> = arrayListOf()

    @SerializedName("list_toppings")
    var listTopping: ArrayList<MenuFood> = arrayListOf()

    var priceAfterPromotion: Double = -1.toDouble()

    constructor() {}

    constructor(name: String) {
        this.name = name
    }

    constructor(id: Int, name: String, price: Double, image: String, description: String) {
        this.id = id
        this.name = name
        this.price = price
        this.image = image
        this.description = description
    }

    constructor(id: Int, name: String?, price: Double, priceMin: Double, priceMax: Double, image: String?, description: String?) {
        this.id = id
        this.name = name
        this.price = price
        this.priceMin = priceMin
        this.priceMax = priceMax
        this.image = image
        this.description = description
    }


    fun setId(id: String) {
        this.id = Integer.parseInt(id)
    }

    fun setPrice(price: String) {
        this.price = price.toDouble()
    }

    fun setPriceMin(priceMin: String) {
        this.priceMin = priceMin.toDouble()
    }

    fun setPriceMax(priceMax: String) {
        this.priceMax = priceMax.toDouble()
    }
}