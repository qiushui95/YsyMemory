package son.ysy.memory.common.http

import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.Koin
import son.ysy.memory.repository.UserRepository

class TokenHeaderInterceptor(koin: Koin) : Interceptor {

    private val userRepository: UserRepository by koin.inject()

    override fun intercept(chain: Interceptor.Chain): Response {
        val currentToken = userRepository.getCurrentToken()
        val newRequest = if (currentToken == null) {
            chain.request()
        } else {
            chain.request().newBuilder()
                .addHeader("token", currentToken)
                .build()
        }

        return chain.proceed(newRequest)
    }
}