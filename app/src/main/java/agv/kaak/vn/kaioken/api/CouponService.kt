package agv.kaak.vn.kaioken.api

import agv.kaak.vn.kaioken.entity.response.DetailCouponResponse
import agv.kaak.vn.kaioken.entity.response.DetailPromotionResponse
import agv.kaak.vn.kaioken.entity.response.GetListCouponUserOnPageResponse
import agv.kaak.vn.kaioken.entity.response.InfoCouponResponse
import io.reactivex.Observable
import retrofit2.http.*

interface CouponService {
    @FormUrlEncoded
    @POST("coupons/code/info")
    fun getDetailCoupon(@Field("page_id") pageId: Int,
                        @Field("coupon_code") couponCode: String, @HeaderMap headers: HashMap<String, String>): Observable<DetailCouponResponse>

    @FormUrlEncoded
    @POST("coupons/user/list")
    fun getListCouponOfUserOnPage(@Field("page_id") pageId: Int,
                                  @Field("customer_id") customerId: Int, @HeaderMap headers: HashMap<String, String>): Observable<GetListCouponUserOnPageResponse>

    @GET("notify/coupon/info")
    fun getDetailCoupon(@Query("coupon_id") couponId: Int, @HeaderMap headers: HashMap<String, String>): Observable<InfoCouponResponse>

    @GET("coupons/detail")
    fun getDetailPromotion(@Query("promotion_id") promotionId: Int, @HeaderMap headers: HashMap<String, String>): Observable<DetailPromotionResponse>
}