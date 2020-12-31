package son.ysy.memory.di

import android.app.Application
import com.tencent.mmkv.MMKV
import org.koin.dsl.module
import son.ysy.memory.App

object MoreModule {
    val moreModule = module {
        single {
            MMKV.initialize(get<Application>())
            MMKV.defaultMMKV(MMKV.SINGLE_PROCESS_MODE, "ysy")
        }
        single {
            getKoin()
        }
    }
}