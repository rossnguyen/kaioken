package agv.kaak.vn.kaioken.api

import agv.kaak.vn.kaioken.entity.response.GetListPromotionResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import java.util.*

interface PromotionService {
    @GET("promotion/news/list")
    fun getListPromotion(@HeaderMap headers: HashMap<String, String>): Observable<GetListPromotionResponse>
}