package son.ysy.memory.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

abstract class BaseRepository {
    protected fun <T> flowWithLast(block: suspend FlowCollector<T>.() -> T): Flow<T> = flow {
        emit(block())
    }
}