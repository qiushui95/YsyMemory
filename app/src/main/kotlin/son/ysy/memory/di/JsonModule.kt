package son.ysy.memory.di

import com.squareup.moshi.Moshi
import org.koin.dsl.module

object JsonModule {
    val jsonModule = module {
        single {
            Moshi.Builder()
                .build()
        }
    }
}