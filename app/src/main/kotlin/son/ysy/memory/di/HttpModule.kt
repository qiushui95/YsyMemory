package son.ysy.memory.di

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import son.ysy.memory.common.http.MoshiConverterFactory
import son.ysy.memory.common.http.OkHttpClientConfigImpl.configMore
import son.ysy.memory.common.http.TokenHeaderInterceptor
import son.ysy.memory.common.json.ResponseResultJsonAdapter

object HttpModule {

    val httpModule = module {
        single {
            OkHttpClient.Builder()
                .addInterceptor(
                    ChuckerInterceptor.Builder(get<Application>())
                        .alwaysReadResponseBody(true)
                        .build()
                )
                .addInterceptor(TokenHeaderInterceptor(get()))
                .configMore()
                .build()
        }

        single {
            Retrofit.Builder()
                .baseUrl("http://www.54ysy.site/")
                .addConverterFactory(
                    MoshiConverterFactory(
                        get(),
                        Moshi.Builder()
                            .add(ResponseResultJsonAdapter.factory)
                            .build()
                    )
                )
                .client(get())
                .build()
        }
    }
}