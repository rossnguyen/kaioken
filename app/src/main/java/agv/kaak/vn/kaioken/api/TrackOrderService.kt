package agv.kaak.vn.kaioken.api

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.response.TrackOrderResponse
import io.reactivex.Observable
import retrofit2.http.*

interface TrackOrderService {
    @GET("user/ordering")
    fun getListOrdering(@HeaderMap headers: HashMap<String, String>): Observable<TrackOrderResponse>

    @GET("user/ordered")
    fun getListOrdered(@Query("offset") offset: Int,
                       @Query("limit") limit: Int, @HeaderMap headers: HashMap<String, String>): Observable<TrackOrderResponse>

    @FormUrlEncoded
    @POST("user/ordered/remove")
    fun removeOrdered(@Field("order_id") orderId: Int, @HeaderMap headers: HashMap<String, String>): Observable<BaseResponse>

    @FormUrlEncoded
    @POST("user/ordered/multi-remove")
    fun removeListOrdered(@Field("list_order_id[]") listOrderId: ArrayList<Int>, @HeaderMap headers: HashMap<String, String>): Observable<BaseResponse>
}