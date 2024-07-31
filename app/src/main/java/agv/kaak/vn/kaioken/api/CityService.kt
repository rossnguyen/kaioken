package agv.kaak.vn.kaioken.api

import agv.kaak.vn.kaioken.entity.response.GetListCityResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap

interface CityService {
    @GET("location/list")
    fun getCityList(@HeaderMap headers: HashMap<String, String>): Observable<GetListCityResponse>
}