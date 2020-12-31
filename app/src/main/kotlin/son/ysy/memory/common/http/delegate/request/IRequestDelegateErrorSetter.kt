package son.ysy.memory.common.http.delegate.request

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import son.ysy.memory.common.error.MessageError

interface IRequestDelegateErrorSetter {
    @MainThread
    fun setError(message: String) {
        setError(MessageError(message))
    }

    fun postError(message: String) {
        postError(MessageError(message))
    }

    @MainThread
    fun setError(error: Throwable)

    fun postError(error: Throwable)
}