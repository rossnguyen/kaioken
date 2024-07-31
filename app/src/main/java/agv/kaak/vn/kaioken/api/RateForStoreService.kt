package agv.kaak.vn.kaioken.api

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.response.ListRatedResponse
import io.reactivex.Observable
import retrofit2.http.*

interface RateForStoreService {
    @FormUrlEncoded
    @POST("page/user/rating")
    fun RateForStore(@Field("page_id") pageId: Int,
                     @Field("rate") rate: Int,
                     @Field("feedback") feedback: String,
                     @Field("order_id") orderId: Int, @HeaderMap headers: HashMap<String, String>): Observable<BaseResponse>

}