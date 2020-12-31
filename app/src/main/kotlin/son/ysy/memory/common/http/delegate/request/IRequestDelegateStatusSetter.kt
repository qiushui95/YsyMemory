package son.ysy.memory.common.http.delegate.request

import androidx.annotation.MainThread
import son.ysy.memory.common.entity.RequestStatus

interface IRequestDelegateStatusSetter<T: Any> {
    @MainThread
    fun setStatus(status: RequestStatus<T>)

    fun postStatus(status: RequestStatus<T>)
}