package son.ysy.memory.http

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object OkHttpClientConfigImpl : OkHttpClientConfig {
    override fun OkHttpClient.Builder.configMore(): OkHttpClient.Builder {
        return addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }
}