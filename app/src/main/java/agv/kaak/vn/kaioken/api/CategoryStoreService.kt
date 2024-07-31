package agv.kaak.vn.kaioken.api

import agv.kaak.vn.kaioken.entity.response.CategoryStoreResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

interface CategoryStoreService {
    @GET("get-list-categories")
    fun getCategoryRestaurantList(@HeaderMap headers: HashMap<String, String>): Observable<CategoryStoreResponse>

    @GET("get-list-categories")
    fun getCategoryRestaurantListLimitOffset(@Query("limit") limit: Int,
                                             @Query("offset") offset: Int, @HeaderMap headers: HashMap<String, String>): Observable<CategoryStoreResponse>
}