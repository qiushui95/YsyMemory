package son.ysy.memory.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import son.ysy.memory.ui.activity.MainViewModel
import son.ysy.memory.ui.splash.SplashViewModel

object ViewModelModule {
    val viewModelModule = module {
        viewModel {
            MainViewModel(get(), get())
        }
        viewModel {
            SplashViewModel(get())
        }
    }
}