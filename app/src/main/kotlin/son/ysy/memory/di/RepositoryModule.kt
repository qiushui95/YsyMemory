package son.ysy.memory.di

import org.koin.dsl.module
import son.ysy.memory.repository.UserRepository

object RepositoryModule {
    val repositoryModule = module {
        single {
            UserRepository(get())
        }
    }
}