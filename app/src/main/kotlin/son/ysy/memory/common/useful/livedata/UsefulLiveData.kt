package son.ysy.memory.common.useful.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.distinctUntilChanged
import com.kunminx.architecture.ui.callback.UnPeekLiveData

class UsefulLiveData<T> private constructor(
    override val setValue: (T) -> Unit,
    override val postValue: (T) -> Unit,
    override val liveData: LiveData<T>
) : UsefulLiveDataSetter<T>, UsefulLiveDataGetter<T> {

    companion object {

        fun <T : Any> mutable() = mutable(MutableLiveData<T>())

        fun <T : Any> mutable(
            liveData: MutableLiveData<T>
        ) = UsefulLiveData(liveData::setValue, liveData::postValue, liveData)

        fun <T : Any> mutableWithInitial(
            initialValue: T
        ) = mutable(MutableLiveData(initialValue))

        fun <T : Any> mutableEmpty() = mutable(MutableLiveData<T>())

        fun <T : Any> unPeek() = UnPeekLiveData<T>().run {
            UsefulLiveData(::setValue, ::postValue, this)
        }

        fun <T : Any> handle(
            key: String,
            handle: SavedStateHandle
        ) = mutable(handle.getLiveData<T>(key))

        fun <T : Any> handleWithInitialValue(
            key: String,
            handle: SavedStateHandle,
            initialValue: T
        ) = mutable(handle.getLiveData(key, initialValue))

        fun <T : Any> handleWithInitialBlock(
            key: String,
            handle: SavedStateHandle,
            initialBlock: () -> T?
        ) = when (val initialValue = initialBlock()) {
            null -> handle<T>(key, handle)
            else -> mutable(handle.getLiveData(key, initialValue))
        }

        fun <T : Any> handleWithInitialBlockAndDefaultValue(
            key: String,
            handle: SavedStateHandle,
            defaultValue: T,
            initialBlock: () -> T?
        ) = when (val initialValue = initialBlock()) {
            null -> mutable(handle.getLiveData(key, defaultValue))
            else -> mutable(handle.getLiveData(key, initialValue))
        }
    }

    override val liveDataDistinct: LiveData<T> by lazy {
        liveData.distinctUntilChanged()
    }

    override val value: T?
        get() = liveData.value
}