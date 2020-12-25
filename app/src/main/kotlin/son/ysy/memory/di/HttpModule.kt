package son.ysy.memory.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

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
                .baseUrl("http://192.168.31.16/")
                .addConverterFactory(MoshiConverterFactory.create(get()))
                .build()
        }
    }
}