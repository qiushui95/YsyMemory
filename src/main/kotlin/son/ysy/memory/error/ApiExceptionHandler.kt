package son.ysy.memory.error

import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(ApiError::class)
    fun handleApiException(ex: ApiError): ErrorInfo = ex.errorInfo
}