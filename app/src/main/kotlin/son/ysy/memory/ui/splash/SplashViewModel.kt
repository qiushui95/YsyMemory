package son.ysy.memory.ui.splash

import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.StringUtils
import org.koin.core.Koin
import son.ysy.creator.annotations.KeyCreator
import son.ysy.memory.R
import son.ysy.memory.common.base.BaseViewModel
import son.ysy.memory.common.http.delegate.request.IRequestDelegateGetter
import son.ysy.memory.common.http.delegate.request.RequestDelegate
import son.ysy.memory.repository.UserRepository
import son.ysy.memory.common.useful.livedata.UsefulLiveData
import son.ysy.memory.utils.RegexUtil

@KeyCreator("stateId", "login", "marker")
class SplashViewModel(handle: SavedStateHandle, koin: Koin) : BaseViewModel() {


    private val userRepository: UserRepository by koin.inject()

    private val stateIdM = UsefulLiveData.handle<Int>(SplashViewModelKeys.stateId, handle)

    val loginStatus by lazy {
        userRepository.loginStatusLiveData
    }

    val stateId: LiveData<Int> by lazy {
        stateIdM.liveDataDistinct
    }

    private val loginRequestDelegate by lazy {
        RequestDelegate.Builder<String>().setByHandle(SplashViewModelKeys.login, handle).build()
    }

    private val markerRequestDelegate by lazy {
        RequestDelegate.Builder<String>().setByHandle(SplashViewModelKeys.marker, handle).build()
    }

    val markerRequestGetter: IRequestDelegateGetter<String>
        get() = markerRequestDelegate

    val loginErrorMessage by lazy {
        loginRequestDelegate.errorMessageLiveData
    }

    fun onStateChanged(@IdRes stateId: Int) {
        stateIdM.setValue(stateId)
    }

    fun onTokenChanged() {
        markerRequestDelegate.startRequest(viewModelScope, userRepository::getMarker)
    }

    fun loginByPhoneAndPassword(phone: String, password: String) {
        if (!RegexUtils.isMobileExact(phone)) {
            loginRequestDelegate.postError(StringUtils.getString(R.string.string_phone_regex_fail))
        } else if (!RegexUtil.isLoginPassword(password)) {
            loginRequestDelegate.postError(StringUtils.getString(R.string.string_login_password_regex_fail))
        } else {
            loginRequestDelegate.startRequest(viewModelScope) {
                userRepository.loginByPhoneAndPassword(phone, password)
            }
        }
    }
}