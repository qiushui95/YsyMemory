package son.ysy.memory.di

import com.tencent.mmkv.MMKV
import org.koin.dsl.module
import son.ysy.memory.App

object MoreModule {
    val moreModule = module {
        single {
            MMKV.initialize(get<App>())
            MMKV.defaultMMKV(MMKV.SINGLE_PROCESS_MODE, "ysy")
        }
        single {
            getKoin()
        }
    }
}