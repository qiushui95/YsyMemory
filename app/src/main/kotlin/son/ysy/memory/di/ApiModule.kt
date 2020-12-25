package son.ysy.memory.di

import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create
import son.ysy.memory.api.LoginApi

object ApiModule {
    val apiModule = module {
        single {
            get<Retrofit>().create<LoginApi>()
        }
    }
}

