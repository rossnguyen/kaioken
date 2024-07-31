package agv.kaak.vn.kaioken.api

import agv.kaak.vn.kaioken.entity.response.GetKiOfPageResponse
import agv.kaak.vn.kaioken.entity.response.GetListKiResponse
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

interface KiService {
    @GET("ki/user/list")
    fun getListKi(@Query("offset") offset: Int,
                  @Query("limit") limit: Int, @HeaderMap headers: HashMap<String, String>): Observable<GetListKiResponse>

    @GET("ki/user/page")
    fun getKiOfPage(@Query("page_id") pageId: Int, @HeaderMap headers: HashMap<String, String>): Observable<GetKiOfPageResponse>
}