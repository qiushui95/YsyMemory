package son.ysy.memory.common.http

import okhttp3.ResponseBody
import org.koin.core.Koin
import retrofit2.Converter
import son.ysy.memory.common.error.ResponseResultError
import son.ysy.memory.repository.UserRepository

class MoshiResponseBodyConverter<T : Any>(
    koin: Koin,
    @Suppress("UNUSED_PARAMETER") clz: Class<T>,
    private val delegateConvert: Converter<ResponseBody, *>
) : Converter<ResponseBody, T> {

    private val userRepository: UserRepository by koin.inject()

    @Suppress("UNCHECKED_CAST")
    override fun convert(value: ResponseBody): T? = try {
        delegateConvert.convert(value) as? T
    } catch (e: Exception) {
        when {
            e is ResponseResultError && e.isLoginInvalid -> {
                userRepository.loginInvalid()
            }
        }
        throw e
    }
}