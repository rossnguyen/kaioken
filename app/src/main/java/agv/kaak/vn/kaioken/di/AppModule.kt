package agv.kaak.vn.kaioken.di

import agv.kaak.vn.kaioken.R
import agv.kaak.vn.kaioken.helper.GlobalHelper
import agv.kaak.vn.kaioken.utils.Constraint
import agv.kaak.vn.kaioken.utils.Constraint.Companion.BASE_IMAGE
import agv.kaak.vn.kaioken.utils.Constraint.Companion.BASE_URL
import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.socket.client.IO
import io.socket.client.Socket
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URI
import java.net.URISyntaxException
import java.sql.Timestamp
import java.util.*
import javax.inject.Named
import javax.inject.Singleton


@Module
class AppModule(val mContext: Context) {
    var gson = GsonBuilder()
            .setLenient()
            .create()

    //private val sharedPreferences = getApplicationContext().getSharedPreferences(Constraint.SHARE_PRE_INFO_USER, Context.MODE_PRIVATE)

    private var client = OkHttpClient.Builder().addInterceptor { chain ->
        val original = chain.request()

        val request = original?.newBuilder()
               /* ?.header("uid", Constraint.uid)
                ?.header("sid", Constraint.sid)
                ?.header("x-kaak-signature", Constraint.signature)*/
                ?.method(original.method(), original.body())
                ?.build()

        chain.proceed(request!!)
    }.build()


    @Singleton
    @Provides
    @Named("api")
    fun provideAPI(): Retrofit {

        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
    }

    @Singleton
    @Provides
    @Named("image")
    fun provideImage(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_IMAGE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
    }

    @Provides
    @Singleton
    fun provideSocket(): Socket {
        try {
            val options = IO.Options()
            val jsonObject = JSONObject()
            jsonObject.put("uid", Constraint.uid)
            jsonObject.put("sid", Constraint.sid)
            options.query = "token=${jsonObject.toString()}"
            options.forceNew = false

            return IO.socket(URI(Constraint.SOCKET_CONNECT_URL), options)

        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
    }

    @Provides
    @Singleton
    fun provideErrorString(): HashMap<Int, String> {
        val errorCodes = mContext.resources.getStringArray(R.array.error_code)
        val result = hashMapOf<Int, String>()
        errorCodes.forEachIndexed { _, s ->
            result[s.split("|")[0].toInt()] = s.split("|")[1]
        }

        return result
    }
}