package son.ysy.memory.util

object TokenUtils {
    private const val KEY_TOKEN_PREFIX = "token"

    fun createTokenKey(token: String) = KEY_TOKEN_PREFIX + token

    fun createPhoneKey(phone: String) = KEY_TOKEN_PREFIX + phone
}