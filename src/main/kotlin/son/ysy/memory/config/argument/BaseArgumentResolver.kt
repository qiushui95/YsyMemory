package son.ysy.memory.config.argument

import org.springframework.core.MethodParameter
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import kotlin.reflect.KClass

abstract class BaseArgumentResolver<P : Any>(private val clz: KClass<P>) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter) = true

    final override fun resolveArgument(
        parameter: MethodParameter,
        bindingContext: BindingContext,
        exchange: ServerWebExchange
    ): Mono<Any> = createArgument(parameter, bindingContext, exchange)

    abstract fun createArgument(
        parameter: MethodParameter,
        bindingContext: BindingContext,
        exchange: ServerWebExchange
    ): Mono<Any>

}