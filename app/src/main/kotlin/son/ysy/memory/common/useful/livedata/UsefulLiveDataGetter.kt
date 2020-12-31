package son.ysy.memory.common.useful.livedata

import androidx.lifecycle.LiveData

interface UsefulLiveDataGetter<T > {

    val liveData: LiveData<T>

    val liveDataDistinct: LiveData<T>

    val value: T?
}