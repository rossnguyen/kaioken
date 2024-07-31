package agv.kaak.vn.kaioken.api

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.response.*
import agv.kaak.vn.kaioken.utils.Constraint
import io.reactivex.Observable
import retrofit2.http.*

interface UserService {
    @POST("user/info")
    fun getUserInfo(@HeaderMap headers: HashMap<String, String>): Observable<UserResponse>

    @FormUrlEncoded
    @POST("user/info/edit")
    fun updateUserInfo(@Field("uid") uid: String,
                       @Field("sid") sid: String,
                       @Field("name") name: String,
                       @Field("email") email: String,
                       @Field("gender") gender: Int,
                       @Field("birth_date") birthday: String,
                       @Field("phone") phone: String, @HeaderMap headers: HashMap<String, String>): Observable<UpdateUserInfoResponse>

    @FormUrlEncoded
    @POST("user/change/password")
    fun changePassword(@Field("old_password") oldPassword: String,
                       @Field("new_password") newPassword: String,
                       @Field("new_password2") confirmNewPassword: String, @HeaderMap headers: HashMap<String, String>): Observable<BaseResponse>

    @FormUrlEncoded
    @POST("user/forgot/password")
    fun forgotPassword(@Field("phone") phone: String,
                       @Field("code") code: String,
                       @Field("token") token: String,
                       @Field("type_id") typeId: Int, @HeaderMap headers: HashMap<String, String>): Observable<LoginResponse>

    @FormUrlEncoded
    @POST("user/token/check")
    fun checkTokenValid(
            @Field("uid") uid: String,
            @Field("sid") sid: String, @HeaderMap headers: HashMap<String, String>): Observable<CheckTokenValidResponse>
}