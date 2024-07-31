package agv.kaak.vn.kaioken.entity

import com.bignerdranch.expandablerecyclerview.Model.ParentObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MenuInfo : Serializable, ParentObject {
    @SerializedName("category_id")
    @Expose
    var categoryId: Int = 0
    @SerializedName("category_name")
    @Expose
    var categoryName: String? = null
    @SerializedName("items")
    @Expose
    var items: List<MenuFood>? = null

    override fun setChildObjectList(p0: MutableList<Any>?) {
        items = p0 as List<MenuFood>
    }

    override fun getChildObjectList(): MutableList<Any> {
        return items as MutableList<Any>
    }
}