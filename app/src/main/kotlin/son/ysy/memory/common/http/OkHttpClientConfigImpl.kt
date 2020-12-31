package son.ysy.memory.common.http

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object OkHttpClientConfigImpl {
    fun OkHttpClient.Builder.configMore(): OkHttpClient.Builder {
        return addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }
}