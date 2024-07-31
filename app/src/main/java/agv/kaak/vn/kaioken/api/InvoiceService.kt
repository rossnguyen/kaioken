package agv.kaak.vn.kaioken.api

import agv.kaak.vn.kaioken.entity.response.InvoiceInfoResponse
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

interface InvoiceService {
    @GET("s1/order/bill/info?")
    fun getBillInfo(@Query("invoice_no") invoiceNo: String, @HeaderMap headers: HashMap<String, String>): Observable<InvoiceInfoResponse>
}