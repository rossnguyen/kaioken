package agv.kaak.vn.kaioken.api

import agv.kaak.vn.kaioken.entity.base.BaseResponse
import agv.kaak.vn.kaioken.entity.response.CheckPhoneExistResponse
import agv.kaak.vn.kaioken.entity.response.RegisterAccountResponse
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface RegisterAccountService {
    @FormUrlEncoded
    @POST("user/firebase/signup")
    fun registerAccount(@Field("phone") phone: String,
                        @Field("token") token: String,
                        @Field("device_type") deviceType: Int,
                        @Field("device_name") deviceName: String,
                        @Field("user-agent") userAgent: String,
                        @Field("name") name: String,
                        @Field("password") password: String,
                        @Field("password2") password2: String, @HeaderMap headers: HashMap<String, String>): Observable<RegisterAccountResponse>

    @FormUrlEncoded
    @POST("user/firebase/exist")
    fun checkPhoneExist(@Field("phone") phone: String,
                        @Field("type_id") typeId: Int, @HeaderMap headers: HashMap<String, String>): Observable<CheckPhoneExistResponse>
}
