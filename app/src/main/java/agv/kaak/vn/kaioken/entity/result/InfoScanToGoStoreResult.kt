package agv.kaak.vn.kaioken.entity.result

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class InfoScanToGoStoreResult {
    @SerializedName("table_id")
    @Expose
    var tableId: Int? = 0

    @SerializedName("page_id")
    @Expose
    var pageId: Int? = 0

    @SerializedName("page_info")
    @Expose
    var dataRestaurant: DetailStoreResult? = null
}