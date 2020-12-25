package son.ysy.memory.repository

import org.koin.core.Koin
import son.ysy.memory.api.LoginApi
import son.ysy.memory.base.BaseRepository
import son.ysy.memory.entity.LoginRequestParam

class UserRepository(koin: Koin) : BaseRepository() {

    private val loginApi: LoginApi by koin.inject()

    fun loginByPhoneAndPassword(phone: String, password: String) = flowWithLast<String> {
        loginApi.loginByPhoneAndPassword(LoginRequestParam(phone, password))
    }
}