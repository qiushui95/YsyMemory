package son.ysy.memory.common.http.delegate.request

import androidx.lifecycle.LiveData

interface IRequestDelegateDataGetter<T: Any> {
    val dataLiveData: LiveData<T>?

    val dataLiveDataDistinct: LiveData<T>?
}