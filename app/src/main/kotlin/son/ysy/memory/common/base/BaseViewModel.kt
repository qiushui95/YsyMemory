package son.ysy.memory.common.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

//    protected fun <T> Flow<T>.dealBusy(
//        busySetter: (Boolean) -> Unit
//    ): Flow<T> = dealBusy(busySetter) { it }
//
//    protected fun <T, R> Flow<T>.dealBusy(
//        busySetter: (R) -> Unit,
//        busyMapper: (Boolean) -> R?
//    ): Flow<T> = onStart {
//        busyMapper(true)?.apply(busySetter)
//    }.onCompletion {
//        busyMapper(false)?.apply(busySetter)
//    }.flowOn(Dispatchers.Main)
//
//    protected fun <T> Flow<T>.dealBusy(
//        usefulLiveData: UsefulLiveData<Boolean>
//    ): Flow<T> = dealBusy(usefulLiveData::setValue)
//
//
//    protected fun <T> Flow<T>.dealError(
//        usefulLiveData: UsefulLiveData<String>
//    ) = dealError(usefulLiveData::setValue)
//
//    protected fun <T> Flow<T>.dealError(
//        liveData: UnPeekLiveData<String>
//    ) = dealError(liveData::setValue)
}