package agv.kaak.vn.kaioken.api

import agv.kaak.vn.kaioken.entity.response.CreateStoreCommentResponse
import agv.kaak.vn.kaioken.entity.response.GetStoreCommentResonse
import io.reactivex.Observable
import retrofit2.http.*
import java.util.ArrayList

interface CommentStoreService {
    @FormUrlEncoded
    @POST("comment/create")
    fun createCommentRestaurant(@Field("uid") uId: String,
                                         @Field("sid") sId: String,
                                         @Field("page_id") pageId: Int,
                                         @Field("user_id") userId: Int,
                                         @Field("content") content: String,
                                         @Field("list_image[]") listLinkImage: ArrayList<String>, @HeaderMap headers: HashMap<String, String>): Observable<CreateStoreCommentResponse>

    @GET("comment/list")
    fun getCommentRestaurant(@Query("page_id") pageId: Int, @HeaderMap headers: HashMap<String, String>): Observable<GetStoreCommentResonse>
}