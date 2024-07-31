package agv.kaak.vn.kaioken.api

import agv.kaak.vn.kaioken.entity.response.GetDetailNewsResponse
import agv.kaak.vn.kaioken.entity.response.GetListNewsResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.Query

interface NewsService {
    @GET("news/list")
    fun getListNews(@HeaderMap headers: HashMap<String, String>): Observable<GetListNewsResponse>

    @GET("news/detail")
    fun getDetailNews(@Query("news_id") newId: Int, @HeaderMap headers: HashMap<String, String>): Observable<GetDetailNewsResponse>
}