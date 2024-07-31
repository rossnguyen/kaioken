package agv.kaak.vn.kaioken.api

import agv.kaak.vn.kaioken.entity.response.UploadImageResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface UploadImageService {
    @Multipart
    @POST("image/upload")

    fun uploadImage(@Part("pid") pid: RequestBody,
                    @Part("type") type: RequestBody,
                    @Part("type_id") type_id: RequestBody,
                    @Part files: MultipartBody.Part, @HeaderMap headers: HashMap<String, String>): Observable<UploadImageResponse>
}