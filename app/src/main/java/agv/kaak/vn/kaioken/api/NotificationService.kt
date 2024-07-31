package agv.kaak.vn.kaioken.api

import agv.kaak.vn.kaioken.entity.response.NotificationResponse
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface NotificationService {
    @FormUrlEncoded
    @POST("notify/notice/tab")
    fun getListNotifyNotice(@Field("user_id") userId: Int,
                            @Field("department_id") departmentId: Int,
                            @Field("page_id") pageId: Int,
                            @Field("offset") offset: Int,
                            @Field("limit") limit: Int, @HeaderMap headers: HashMap<String, String>): Observable<NotificationResponse>
}