package son.ysy.memory.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.squareup.moshi.Moshi
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import son.ysy.memory.http.MoshiConverterFactory
import son.ysy.memory.json.ResponseResultJsonAdapter

object HttpModule {

    val httpModule = module {
        single {
            OkHttpClient.Builder()
                .addInterceptor(
                    ChuckerInterceptor.Builder(get())
                        .alwaysReadResponseBody(true)
                        .build()
                )
                .build()
        }

        single {
            Retrofit.Builder()
                .baseUrl("http://www.54ysy.site/")
                .addConverterFactory(
                    MoshiConverterFactory(
                        Moshi.Builder()
                            .add(ResponseResultJsonAdapter.factory)
                            .build()
                    )
                )
                .build()
        }
    }
}