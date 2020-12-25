package son.ysy.memory.http

import okhttp3.OkHttpClient

object OkHttpClientConfigImpl : OkHttpClientConfig {
    override fun OkHttpClient.Builder.configMore(): OkHttpClient.Builder {
        return this
    }
}