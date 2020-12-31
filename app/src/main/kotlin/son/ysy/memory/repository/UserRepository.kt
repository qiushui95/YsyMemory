package son.ysy.memory.repository

import androidx.lifecycle.LiveData
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import org.koin.core.Koin
import son.ysy.creator.annotations.KeyCreator
import son.ysy.memory.api.LoginApi
import son.ysy.memory.api.UserApi
import son.ysy.memory.common.base.BaseRepository
import son.ysy.memory.entity.LoginRequestParam
import son.ysy.memory.common.entity.LoginStatus
import son.ysy.memory.common.useful.livedata.UsefulLiveData

@KeyCreator("tokenInMmkv")
class UserRepository(koin: Koin) : BaseRepository() {

    private val loginApi: LoginApi by koin.inject()

    private val userApi: UserApi by koin.inject()

    private val mmkv: MMKV by koin.inject()

    private val loginStatusLiveDataM = UsefulLiveData.mutable<LoginStatus>()

    init {
        (mmkv.decodeString(UserRepositoryKeys.tokenInMmkv)
            ?.run {
                LoginStatus.LoginIn(this)
            } ?: LoginStatus.NoLogin)
            .apply(loginStatusLiveDataM.setValue)
    }

    val loginStatusLiveData: LiveData<LoginStatus>
        get() = loginStatusLiveDataM.liveDataDistinct

    fun getCurrentToken() = (loginStatusLiveDataM.value as? LoginStatus.LoginIn)?.token

    fun loginByPhoneAndPassword(
        phone: String,
        password: String
    ) = flowWithLast<String> {
        loginApi.loginByPhoneAndPassword(LoginRequestParam(phone, password))
    }.onEach {
        setNewToken(it)
    }.onStart {
        loginStatusLiveDataM.postValue(LoginStatus.LoginPosting)
    }.catch {
        loginStatusLiveDataM.postValue(LoginStatus.Logout)
        throw it
    }.onEach {
        loginStatusLiveDataM.postValue(LoginStatus.LoginIn(it))
    }

    private fun setNewToken(token: String?) {
        val loginStatus = token?.run {
            LoginStatus.LoginIn(this)
        } ?: LoginStatus.NoLogin
        loginStatusLiveDataM.postValue(loginStatus)
        mmkv.encode(UserRepositoryKeys.tokenInMmkv, token)
    }

    fun getMarker() = flowWithLast<String> {
        userApi.getMarker()
    }

    fun loginInvalid() {
        setNewToken(null)
    }
}