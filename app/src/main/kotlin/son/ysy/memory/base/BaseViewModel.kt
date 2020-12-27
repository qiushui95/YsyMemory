package son.ysy.memory.base

import androidx.lifecycle.ViewModel
import com.kunminx.architecture.ui.callback.ProtectedUnPeekLiveData
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import son.ysy.memory.R
import son.ysy.memory.error.MessageError
import son.ysy.memory.useful.UsefulLiveData

abstract class BaseViewModel : ViewModel() {

    protected fun <T> Flow<T>.dealBusy(
        busySetter: (Boolean) -> Unit
    ): Flow<T> = dealBusy(busySetter) { it }

    protected fun <T, R> Flow<T>.dealBusy(
        busySetter: (R) -> Unit,
        busyMapper: (Boolean) -> R?
    ): Flow<T> = onStart {
        busyMapper(true)?.apply(busySetter)
    }.onCompletion {
        busyMapper(false)?.apply(busySetter)
    }.flowOn(Dispatchers.Main)

    protected fun <T> Flow<T>.dealBusy(
        usefulLiveData: UsefulLiveData<Boolean>
    ): Flow<T> = dealBusy(usefulLiveData::setValue)

    protected fun <T> Flow<T>.dealError(messageSetter: (String) -> Unit): Flow<T> = catch { error ->
        when (error) {
            is MessageError -> messageSetter(error.message ?: "no message!!")
            else -> {
                error.printStackTrace()
            }
        }
    }.flowOn(Dispatchers.Main)

    protected fun <T> Flow<T>.dealError(
        usefulLiveData: UsefulLiveData<String>
    ) = dealError(usefulLiveData::setValue)

    protected fun <T> Flow<T>.dealError(
        liveData: UnPeekLiveData<String>
    ) = dealError(liveData::setValue)
}