package son.ysy.memory.config.argument

import kotlinx.coroutines.reactor.mono
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.reactive.BindingContext
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import son.ysy.memory.entity.UserId
import son.ysy.memory.error.param.HeaderMissError
import son.ysy.memory.error.token.TokenIncorrectError
import son.ysy.memory.other.Constants
import son.ysy.memory.util.RedisKeyUtil

@Configuration
class UserIdResolver : BaseArgumentResolver<UserId>(UserId::class) {

    @Autowired
    private lateinit var redis: StringRedisTemplate

    override fun createArgument(
        parameter: MethodParameter,
        bindingContext: BindingContext,
        exchange: ServerWebExchange
    ): Mono<Any> = mono {
        val token = exchange.request.headers[Constants.KEY_TOKEN]?.firstOrNull()
        token?.run {
            redis.opsForHash<String, String>()[RedisKeyUtil.KEY_TOKEN, this]
        }?.run { UserId(this) }
            ?: throw when (token) {
                null -> HeaderMissError(Constants.KEY_TOKEN)
                else -> TokenIncorrectError()
            }
    }
}