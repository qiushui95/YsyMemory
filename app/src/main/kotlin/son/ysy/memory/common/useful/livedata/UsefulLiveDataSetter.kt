package son.ysy.memory.common.useful.livedata

interface UsefulLiveDataSetter<T > {

    val setValue: (T) -> Unit

    val postValue: (T) -> Unit
}