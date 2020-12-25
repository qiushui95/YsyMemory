package son.ysy.memory

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.tencent.mmkv.MMKV
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import son.ysy.memory.di.*
import son.ysy.memory.ui.activity.MainViewModel
import son.ysy.memory.ui.splash.SplashViewModel

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Utils.init(this)

        startKoin {
            modules(
                ApiModule.apiModule,
                HttpModule.httpModule,
                JsonModule.jsonModule,
                ViewModelModule.viewModelModule,
                RepositoryModule.repositoryModule,
                MoreModule.moreModule,
                module {
                    single {
                        this@App
                    }
                }
            )
        }
    }
}