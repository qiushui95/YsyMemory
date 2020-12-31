package son.ysy.memory.common.http.delegate.request

import androidx.annotation.MainThread

interface IRequestDelegateDataSetter<T: Any> {
    @MainThread
    fun setData(data: T)

    fun postData(data: T)
}