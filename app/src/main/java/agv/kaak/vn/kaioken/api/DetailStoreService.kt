package agv.kaak.vn.kaioken.api

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.response.*
import io.reactivex.Observable
import retrofit2.http.*

interface DetailStoreService {
    @GET("page/info")
    fun getInfoRestaurant(@Query("page_id") pageId: Int,
                          @Query("uid") uid: Int, @HeaderMap headers: HashMap<String, String>): Observable<DetailStoreResponse>

    @FormUrlEncoded
    @POST("page/user/follow")
    fun followRestaurant(@Field("user_id") userId: Int,
                         @Field("page_id") pageId: Int, @HeaderMap headers: HashMap<String, String>): Observable<BaseResponse>

    @GET("page/image/list")
    fun getImageDetail(@Query("page_id") pageId: Int, @HeaderMap headers: HashMap<String, String>): Observable<ImageDetailStoreResponse>


    @GET("s1/menu/item/detail")
    fun getImageDetailFood(@Query("menu_item_id") menuId: Int, @HeaderMap headers: HashMap<String, String>): Observable<MenuItemDetail>

    @FormUrlEncoded
    @POST("qr/scan")
    fun getInfoRestaurantFromQR(@Field("sig") sig: String,
                                @Field("lat") lat: Double,
                                @Field("long") lng: Double, @HeaderMap headers: HashMap<String, String>): Observable<ScanQRToGoStoreResponse>

    @GET("recruitment/detail")
    fun getDetailRecruitment(@Query("page_id") pageId: Int, @HeaderMap headers: HashMap<String, String>): Observable<DetailRecruitmentResponse>

    @GET("page/promotion/list-valid")
    fun getListPromotion(@Query("page_id") pageId: Int, @HeaderMap headers: HashMap<String, String>): Observable<GetListStorePromotionResponse>

    @FormUrlEncoded
    @POST("order-form/page/list-paid")
    fun getOrderFormPaid(@Field("page_id") pageId: Int, @HeaderMap headers: HashMap<String, String>): Observable<GetListOrderFormPaidResponse>
}