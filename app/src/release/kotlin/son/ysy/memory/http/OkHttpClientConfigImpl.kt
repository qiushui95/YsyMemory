package son.ysy.memory.http

import android.util.Log
import com.blankj.utilcode.util.LogUtils
import okhttp3.OkHttpClient

object OkHttpClientConfigImpl : OkHttpClientConfig {
    override fun OkHttpClient.Builder.configMore(): OkHttpClient.Builder {
        Log.e("======","release")
        return this
    }
}