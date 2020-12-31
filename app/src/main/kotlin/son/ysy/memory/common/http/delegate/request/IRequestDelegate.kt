package son.ysy.memory.common.http.delegate.request

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

interface IRequestDelegate<T:Any> {

    fun startRequest(scope: CoroutineScope, requestCreator: () -> Flow<T>)

    fun startRequest(
        scope: CoroutineScope,
        cancelBeforeIfBusy: Boolean?,
        cancelCurrentIfBusy: Boolean?,
        waitBeforeRequestComplete: Boolean?,
        parentJob: Job?,
        requestCreator: () -> Flow<T>
    )
}