package son.ysy.memory.common.entity

sealed class RequestStatus<T : Any>(
    val isBusy: Boolean,
    val isSuccess: Boolean,
    val isFailed: Boolean,
    val isComplete: Boolean,
) {
    data class Success<T : Any>(val data: Any) : RequestStatus<T>(false, true, false, true)

    data class Error<T : Any>(
        val data: Any?,
        val error: Throwable,
        val message: String
    ) : RequestStatus<T>(false, false, true, true)

    data class Busy<T : Any>(val data: Any?) : RequestStatus<T>(true, false, false, false)
}
