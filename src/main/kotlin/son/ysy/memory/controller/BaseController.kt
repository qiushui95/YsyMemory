package son.ysy.memory.controller

import reactor.core.publisher.Mono
import son.ysy.memory.entity.ResponseResult
import kotlin.reflect.full.isSubclassOf

abstract class BaseController {
    protected inline fun <reified T : Any> Mono<T>.toResponseResult(): Mono<*> {
        return this.takeIf { T::class.isSubclassOf(ResponseResult::class) } ?: map {
            ResponseResult.Success(it)
        }
    }
}