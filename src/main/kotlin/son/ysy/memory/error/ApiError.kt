package son.ysy.memory.error

open class ApiError(val code: Int = COMMON_CODE, val msg: String, val data: Any?) : RuntimeException(msg) {
    companion object {
        const val COMMON_CODE = 1024
    }
}