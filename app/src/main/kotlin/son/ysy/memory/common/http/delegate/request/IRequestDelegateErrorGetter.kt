package son.ysy.memory.common.http.delegate.request

import androidx.lifecycle.LiveData

interface IRequestDelegateErrorGetter {
    val errorLiveData: LiveData<Throwable>?

    val errorLiveDataDistinct: LiveData<Throwable>?

    val errorMessageLiveData: LiveData<String>?

    val errorMessageLiveDataDistinct: LiveData<String>?
}