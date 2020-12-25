package son.ysy.memory.http

import okhttp3.OkHttpClient

interface OkHttpClientConfig {

    fun OkHttpClient.Builder.configMore(): OkHttpClient.Builder
}