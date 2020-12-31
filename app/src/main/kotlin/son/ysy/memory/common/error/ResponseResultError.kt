package son.ysy.memory.common.error

class ResponseResultError(
    val code: Int,
    override val message: String
) : MessageError(message) {
    val isLoginInvalid: Boolean = code == 1001
}