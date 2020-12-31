package son.ysy.memory.common.http.delegate.request

import androidx.lifecycle.LiveData

interface IRequestDelegateBusyGetter {
    val busyLiveData: LiveData<Boolean>?

    val busyLiveDataDistinct: LiveData<Boolean>?
}