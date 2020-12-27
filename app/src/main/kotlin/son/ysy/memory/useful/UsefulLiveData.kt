package son.ysy.memory.useful

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.distinctUntilChanged

class UsefulLiveData<T : Any> private constructor(private val liveDataM: MutableLiveData<T>) {

    constructor(
        key: String,
        handle: SavedStateHandle,
        initialValueGetter: (key: String) -> T?
    ) : this(
        when (val initialValue = initialValueGetter(key)) {
            null -> handle.getLiveData(key)
            else -> handle.getLiveData(key, initialValue)
        }
    )

    constructor(
        key: String,
        handle: SavedStateHandle,
        defaultValue: T,
        initialValueGetter: (key: String) -> T?
    ) : this(
        when (val initialValue = initialValueGetter(key)) {
            null -> handle.getLiveData(key, defaultValue)
            else -> handle.getLiveData(key, initialValue)
        }
    )


    constructor(key: String, handle: SavedStateHandle) : this(handle.getLiveData(key))

    constructor(initialValue: T) : this(MutableLiveData(initialValue))

    constructor() : this(MutableLiveData())

    val liveData: LiveData<T> = liveDataM

    val liveDataDistinct: LiveData<T> by lazy {
        liveDataM.distinctUntilChanged()
    }

    fun postValue(value: T) = liveDataM.postValue(value)

    fun setValue(value: T) {
        liveDataM.value = value
    }
}