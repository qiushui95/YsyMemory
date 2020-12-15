package son.ysy.memory.error

open class ApiError(val code: Int = COMMON_CODE, val msg: String) : RuntimeException(msg) {
    companion object {
        const val COMMON_CODE = 1024
    }

    val errorInfo: ErrorInfo
        get() = ErrorInfo(code, msg)
}