package agv.kaak.vn.kaioken.api

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.response.CheckVersionResponse
import agv.kaak.vn.kaioken.entity.response.LoginAnonymousResponse
import agv.kaak.vn.kaioken.entity.response.LoginResponse
import io.reactivex.Observable
import retrofit2.http.*

interface LoginService {
    @FormUrlEncoded
    @POST("user/firebase/login")
    fun login(@Field("phone") phone: String,
              @Field("password") password: String,
              @Field("type_id") typeID: Int,
              @Field("device_name") deviceName: String,
              @Field("device_type") deviceType: Int,
              @Field("user_agent") userAgent: String, @HeaderMap headers: HashMap<String, String>): Observable<LoginResponse>

    @FormUrlEncoded
    @POST("user/anonymous/login")
    fun loginAnonymous(@Field("device_name") deviceName: String,
                       @Field("device_type") deviceType: Int,
                       @Field("user_agent") userAgent: String,
                       @Field("type_id") typeId: Int, @HeaderMap headers: HashMap<String, String>): Observable<LoginAnonymousResponse>

    @FormUrlEncoded
    @POST("public/app/version")
    fun checkVersionLogin(@Field("v_current") version: String,
                          @Field("app_type_id") appTypeId: Int, @HeaderMap headers: HashMap<String, String>): Observable<CheckVersionResponse>

    @FormUrlEncoded
    @POST("user/signin")
    fun loginViaGmail(@Field("email") email: String,
                      @Field("password") password: String,
                      @Field("device_name") deviceName: String,
                      @Field("device_type") deviceType: Int,
                      @Field("user_agent") deviceDetail: String, @HeaderMap headers: HashMap<String, String>): Observable<LoginResponse>

    @FormUrlEncoded
    @POST("user/loginfb")
    fun loginViaFacebook(@Field("token") token: String, @HeaderMap headers: HashMap<String, String>): Observable<LoginResponse>

    @FormUrlEncoded
    @POST("user/update/cloudtoken")
    fun updateCloudToken(@Field("cloud_message_token") token: String, @HeaderMap headers: HashMap<String, String>): Observable<LoginResponse>

    @FormUrlEncoded
    @POST("user/loging")
    fun loginViaGmail(@Field("code") token: String, @HeaderMap headers: HashMap<String, String>): Observable<LoginResponse>

    @GET("user/logout")
    fun logout(@HeaderMap headers: HashMap<String, String>): Observable<BaseResponse>
}