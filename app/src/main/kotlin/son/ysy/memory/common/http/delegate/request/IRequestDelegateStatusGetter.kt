package son.ysy.memory.common.http.delegate.request

import androidx.lifecycle.LiveData
import son.ysy.memory.common.entity.RequestStatus

interface IRequestDelegateStatusGetter<T: Any> {
    val statusLiveData: LiveData<RequestStatus<T>>?

    val statusLiveDataDistinct: LiveData<RequestStatus<T>>?
}