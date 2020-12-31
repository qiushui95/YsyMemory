package son.ysy.memory.common.http.delegate.request

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import son.ysy.creator.annotations.KeyCreator
import son.ysy.memory.common.entity.RequestStatus
import son.ysy.memory.common.error.displayMessage
import son.ysy.memory.common.useful.livedata.UsefulLiveData

@KeyCreator("busy", "data", "error", "errorMessage", "status")
class RequestDelegate<T : Any> private constructor(
    private val config: Config<T>
) : IRequestDelegate<T>, IRequestDelegateGetter<T>, IRequestDelegateSetter<T> {

    private class Config<T : Any>(
        val cancelBeforeIfBusy: Boolean,
        val cancelCurrentIfBusy: Boolean,
        val waitBeforeRequestComplete: Boolean,
        val parentJob: Job,
    ) {
        var busyLiveData: UsefulLiveData<Boolean>? = null
        var errorLiveData: UsefulLiveData<Throwable>? = null
        var errorMessageLiveData: UsefulLiveData<String>? = null
        var dataLiveData: UsefulLiveData<T>? = null
        var statusLiveData: UsefulLiveData<RequestStatus<T>>? = null
    }

    class Builder<T : Any>(
        cancelBeforeIfBusy: Boolean = false,
        cancelCurrentIfBusy: Boolean = true,
        waitBeforeRequestComplete: Boolean = false,
        parentJob: Job = SupervisorJob(),
    ) {

        private val config = Config<T>(
            cancelBeforeIfBusy,
            cancelCurrentIfBusy,
            waitBeforeRequestComplete,
            parentJob
        )

        fun setByHandle(
            key: String,
            handle: SavedStateHandle,
            includeBusy: Boolean = true,
            isBusyUnPeek: Boolean = false,
            includeError: Boolean = false,
            isErrorUnPeek: Boolean = true,
            includeErrorMessage: Boolean = true,
            isErrorMessageUnPeek: Boolean = true,
            includeData: Boolean = true,
            isDataUnPeek: Boolean = false,
            includeStatus: Boolean = false,
            isStatusUnPeek: Boolean = false,
        ): Builder<T> {
            setHandleLiveData(
                includeBusy,
                isBusyUnPeek,
                key,
                handle,
                RequestDelegateKeys.busy,
                ::setBusyLiveData
            )
            setHandleLiveData(
                includeError,
                isErrorUnPeek,
                key,
                handle,
                RequestDelegateKeys.error,
                ::setErrorLiveData
            )
            setHandleLiveData(
                includeErrorMessage,
                isErrorMessageUnPeek,
                key,
                handle,
                RequestDelegateKeys.errorMessage,
                ::setErrorMessageLiveData
            )
            setHandleLiveData(
                includeData,
                isDataUnPeek,
                key,
                handle,
                RequestDelegateKeys.data,
                ::setDataLiveData
            )
            setHandleLiveData(
                includeStatus,
                isStatusUnPeek,
                key,
                handle,
                RequestDelegateKeys.status,
                ::setStatusLiveData
            )
            return this
        }


        private fun <T2 : Any> setHandleLiveData(
            include: Boolean,
            isUnPeek: Boolean,
            key: String,
            handle: SavedStateHandle,
            keyPostfix: String,
            setBlock: (UsefulLiveData<T2>) -> Unit
        ) {
            if (include) {
                if (isUnPeek) {
                    UsefulLiveData.unPeek<T2>()
                } else {
                    UsefulLiveData.handle<T2>(key + keyPostfix, handle)
                }.apply(setBlock)
            }
        }

        fun setByMutable(
            includeBusy: Boolean = true,
            isBusyUnPeek: Boolean = false,
            includeError: Boolean = false,
            isErrorUnPeek: Boolean = true,
            includeErrorMessage: Boolean = true,
            isErrorMessageUnPeek: Boolean = true,
            includeData: Boolean = true,
            isDataUnPeek: Boolean = false,
            includeStatus: Boolean = false,
            isStatusUnPeek: Boolean = false,
        ): Builder<T> {
            setMutableLiveData(includeBusy, isBusyUnPeek, ::setBusyLiveData)
            setMutableLiveData(includeError, isErrorUnPeek, ::setErrorLiveData)
            setMutableLiveData(includeErrorMessage, isErrorMessageUnPeek, ::setErrorMessageLiveData)
            setMutableLiveData(includeData, isDataUnPeek, ::setDataLiveData)
            setMutableLiveData(includeStatus, isStatusUnPeek, ::setStatusLiveData)
            return this
        }

        private fun <T2 : Any> setMutableLiveData(
            include: Boolean,
            isUnPeek: Boolean,
            setBlock: (UsefulLiveData<T2>) -> Unit
        ) {
            if (include) {
                if (isUnPeek) {
                    UsefulLiveData.unPeek<T2>()
                } else {
                    UsefulLiveData.mutable<T2>()
                }.apply(setBlock)
            }
        }

        fun setBusyLiveData(liveData: UsefulLiveData<Boolean>): Builder<T> {
            config.busyLiveData = liveData
            return this
        }

        fun setErrorLiveData(liveData: UsefulLiveData<Throwable>): Builder<T> {
            config.errorLiveData = liveData
            return this
        }

        fun setErrorMessageLiveData(liveData: UsefulLiveData<String>): Builder<T> {
            config.errorMessageLiveData = liveData
            return this
        }

        fun setDataLiveData(liveData: UsefulLiveData<T>): Builder<T> {
            config.dataLiveData = liveData
            return this
        }

        fun setStatusLiveData(liveData: UsefulLiveData<RequestStatus<T>>): Builder<T> {
            config.statusLiveData = liveData
            return this
        }

        fun build() = RequestDelegate(config)
    }

    override val busyLiveData: LiveData<Boolean>?
        get() = config.busyLiveData?.liveData

    override val busyLiveDataDistinct: LiveData<Boolean>?
        get() = config.busyLiveData?.liveDataDistinct

    override val errorLiveData: LiveData<Throwable>?
        get() = config.errorLiveData?.liveData

    override val errorLiveDataDistinct: LiveData<Throwable>?
        get() = config.errorLiveData?.liveDataDistinct

    override val errorMessageLiveData: LiveData<String>?
        get() = config.errorMessageLiveData?.liveData

    override val errorMessageLiveDataDistinct: LiveData<String>?
        get() = config.errorMessageLiveData?.liveDataDistinct

    override val dataLiveData: LiveData<T>?
        get() = config.dataLiveData?.liveData

    override val dataLiveDataDistinct: LiveData<T>?
        get() = config.dataLiveData?.liveDataDistinct

    override val statusLiveData: LiveData<RequestStatus<T>>?
        get() = config.statusLiveData?.liveData

    override val statusLiveDataDistinct: LiveData<RequestStatus<T>>?
        get() = config.statusLiveData?.liveDataDistinct

    @MainThread
    override fun setBusy(busy: Boolean) {
        config.busyLiveData?.setValue?.invoke(busy)

    }

    override fun postBusy(busy: Boolean) {
        config.busyLiveData?.postValue?.invoke(busy)
    }

    @MainThread
    override fun setError(error: Throwable) {
        config.errorLiveData?.setValue?.invoke(error)
        config.errorMessageLiveData?.setValue?.invoke(error.displayMessage)
    }

    override fun postError(error: Throwable) {
        config.errorLiveData?.postValue?.invoke(error)
        config.errorMessageLiveData?.postValue?.invoke(error.displayMessage)
    }

    @MainThread
    override fun setData(data: T) {
        config.dataLiveData?.setValue?.invoke(data)
    }

    override fun postData(data: T) {
        config.dataLiveData?.postValue?.invoke(data)
    }

    @MainThread
    override fun setStatus(status: RequestStatus<T>) {
        config.statusLiveData?.setValue?.invoke(status)

    }

    override fun postStatus(status: RequestStatus<T>) {
        config.statusLiveData?.postValue?.invoke(status)
    }

    override fun startRequest(
        scope: CoroutineScope,
        requestCreator: () -> Flow<T>
    ) = startRequest(scope, null, null, null, null, requestCreator)

    override fun startRequest(
        scope: CoroutineScope,
        cancelBeforeIfBusy: Boolean?,
        cancelCurrentIfBusy: Boolean?,
        waitBeforeRequestComplete: Boolean?,
        parentJob: Job?,
        requestCreator: () -> Flow<T>
    ) {
        val cancelBefore = cancelBeforeIfBusy ?: config.cancelBeforeIfBusy
        val cancelCurrent = cancelCurrentIfBusy ?: config.cancelCurrentIfBusy
        val waitBefore = waitBeforeRequestComplete
            ?: config.waitBeforeRequestComplete
        val job = parentJob ?: config.parentJob

        val isBusy = job.children.count() > 0

        if (cancelBefore && isBusy) {
            job.children.forEach {
                it.cancel()
            }
        }

        if (cancelCurrent && isBusy) {
            return
        }

        scope.launch(Dispatchers.Main) {
            if (waitBefore) {
                job.children.forEach {
                    it.join()
                }
            }

            val oldData = when (val oldStatus = config.statusLiveData?.value) {
                is RequestStatus.Busy -> oldStatus.data
                is RequestStatus.Error -> oldStatus.data
                is RequestStatus.Success -> oldStatus.data
                else -> null
            }

            requestCreator().flowOn(Dispatchers.IO)
                .onStart {
                    setBusy(true)
                    setStatus(RequestStatus.Busy(oldData))
                }.onCompletion {
                    setBusy(false)
                }.catch {
                    it.printStackTrace()
                    setError(it)
                    setStatus(RequestStatus.Error(oldData, it, it.displayMessage))
                }.collect {
                    setData(it)
                    setStatus(RequestStatus.Success(it))
                }
        }
    }
}