package son.ysy.memory.common.error

import com.blankj.utilcode.util.StringUtils
import son.ysy.memory.R
import java.net.SocketException
import java.net.SocketTimeoutException

val Throwable.displayMessage: String
    get() = when (this) {
        is MessageError -> message
        is SocketTimeoutException -> StringUtils.getString(R.string.string_request_timeout)
        is SocketException -> StringUtils.getString(R.string.string_request_timeout)
        else -> message ?: "no message"
    }