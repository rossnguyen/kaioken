package agv.kaak.vn.kaioken.api

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.response.StoreOnMapResponse
import io.reactivex.Observable
import retrofit2.http.*

interface StoreOnMapService {
    @FormUrlEncoded
    @POST("get-list-pages")
    fun getListStoreOnmap(@Field("latitude") lat: Double,
                          @Field("longitude") lng: Double,
                          @Field("keyword") keyword: String,
                          @Field("category_id") category_id: Int,
                          @Field("distance") distance: Int,
                          @Field("is_using_app") is_using_app: Int,
                          @Field("is_shipping") is_shipping: Int,
                          @Field("is_online") is_online: Int,
                          @Field("rating") rating: Int,
                          @Field("offset") offset: Int,
                          @Field("limit") limit: Int, @HeaderMap headers: HashMap<String, String>): Observable<StoreOnMapResponse>

    @GET("get-list-follow")
    fun getListStoreFollow(@Query("uid") uid: Int,
                           @Query("latitude") lat: Double,
                           @Query("longitude") lng: Double,
                           @Query("offset") offset: Int,
                           @Query("limit") limit: Int, @HeaderMap headers: HashMap<String, String>): Observable<StoreOnMapResponse>

    @FormUrlEncoded
    @POST("get-list-pages")
    fun getListStoreOffer(@Field("latitude") lat: Double,
                          @Field("longitude") lng: Double,
                          @Field("distance") distance: Int,
                          @Field("offset") offset: Int,
                          @Field("limit") limit: Int, @HeaderMap headers: HashMap<String, String>): Observable<StoreOnMapResponse>

    @FormUrlEncoded
    @POST("update/login/info")
    fun sendLocationAtFirst(@Field("latitude") lat: Double,
                            @Field("longitude") lng: Double, @HeaderMap headers: HashMap<String, String>): Observable<BaseResponse>
}