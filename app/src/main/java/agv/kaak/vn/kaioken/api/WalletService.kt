package agv.kaak.vn.kaioken.api

import agv.kaak.vn.kaioken.entity.response.wallet.*
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by shakutara on 27/12/2017.
 */
interface WalletService {

    @FormUrlEncoded
    @POST("money/wallet-info")
    fun getWalletInfo(
            @Field("date") dateText: String?,
            @Field("type_id") typeId: Int?,
            @Field("page_id") storeId: Int?,
            @Field("offset") offset: Int?,
            @Field("limit") limit: Int?, @HeaderMap headers: HashMap<String, String>): Observable<WalletResponse>

    @FormUrlEncoded
    @POST("money/wallet-type/list")
    fun getWalletListViaType(
            @Field("date") dateText: String?,
            @Field("type_id") typeId: Int?,
            @Field("page_id") storeId: Int?,
            @Field("wallet_type") walletType: Int?,
            @Field("offset") offset: Int?,
            @Field("limit") limit: Int?, @HeaderMap headers: HashMap<String, String>): Observable<WalletListViaTypeResponse>

    @FormUrlEncoded
    @POST("money/saveWalletLimit")
    fun saveWalletLimit(
            @Field("wallet_limit") money: String,
            @Field("page_id") pageId: Int?,
            @Field("type_id") typeId: Int?, @HeaderMap headers: HashMap<String, String>): Observable<WalletLimitResponse>

    @FormUrlEncoded
    @POST("money/saveWallet")
    fun saveWallet(
            @Field("wallet_type") walletType: String?,
            @Field("title") title: String?,
            @Field("amount") amount: String?,
            @Field("date") dateText: String?,
            @Field("type_id") typeId: Int?,
            @Field("page_id") storeId: Int?, @HeaderMap headers: HashMap<String, String>): Observable<CreateWalletResponse>

    @FormUrlEncoded
    @POST("money/remove-wallet-info")
    fun deleteMoney(
            @Field("id") itemWalletId: String?, @HeaderMap headers: HashMap<String, String>): Observable<CreateWalletResponse>

    @FormUrlEncoded
    @POST("money/wallet/analytic")
    fun getFinanceAnalytic(
            @Field("id") id: Int?,
            @Field("type_id") typeId: Int?,
            @Field("year") year: Int?, @HeaderMap headers: HashMap<String, String>): Observable<DetailWalletResponse>

    @GET("order/bill/info")
    fun getBillDetailViaInvoiceNo(
            @Query("invoice_no") invoiceNo: String?, @HeaderMap headers: HashMap<String, String>): Observable<BillDetailInfoResponse>

}