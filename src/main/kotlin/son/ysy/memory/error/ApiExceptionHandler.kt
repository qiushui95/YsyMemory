package son.ysy.memory.error

import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import son.ysy.memory.entity.ResponseResult


@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(ApiError::class)
    fun handleApiException(ex: ApiError) = ResponseResult.Error(ex)
}