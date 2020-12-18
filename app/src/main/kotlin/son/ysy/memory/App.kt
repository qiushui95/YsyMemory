package son.ysy.memory

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.tencent.mmkv.MMKV
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import son.ysy.memory.ui.activity.MainViewModel
import son.ysy.memory.ui.splash.SplashViewModel

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Utils.init(this)

        startKoin {
            modules(module {
                single {
                    MMKV.initialize(applicationContext)
                    MMKV.defaultMMKV(MMKV.SINGLE_PROCESS_MODE, "ysy")
                }
                single {
                    getKoin()
                }
                viewModel {
                    MainViewModel(get(), get())
                }
                viewModel {
                    SplashViewModel(get())
                }
            })
        }
    }
}