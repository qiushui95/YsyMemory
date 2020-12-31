package son.ysy.memory.common.http.delegate.request

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData

interface IRequestDelegateBusySetter {
    @MainThread
    fun setBusy(busy: Boolean)

    fun postBusy(busy: Boolean)
}