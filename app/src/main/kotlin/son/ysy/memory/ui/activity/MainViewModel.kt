package son.ysy.memory.ui.activity

import androidx.lifecycle.*
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.StringUtils
import com.kunminx.architecture.ui.callback.ProtectedUnPeekLiveData
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.Koin
import son.ysy.creator.annotations.KeyCreator
import son.ysy.memory.R
import son.ysy.memory.base.BaseViewModel
import son.ysy.memory.entity.LoginStatus
import son.ysy.memory.repository.UserRepository
import son.ysy.memory.useful.UsefulLiveData
import son.ysy.memory.utils.RegexUtil

@KeyCreator(keys = ["savedToken"])
class MainViewModel(handle: SavedStateHandle, koin: Koin) : BaseViewModel() {

    private val mmkv: MMKV by koin.inject()

    private val userRepository: UserRepository by koin.inject()

    private val loginStatusLiveDataM = UsefulLiveData<LoginStatus>(
        MainViewModelKeys.savedToken,
        handle
    )

    val loginStatusLiveData by lazy {
        loginStatusLiveDataM.liveDataDistinct
    }

    private val loginErrorMessageM = UnPeekLiveData.Builder<String>().create()

    val loginErrorMessage: ProtectedUnPeekLiveData<String> = loginErrorMessageM

    val hasLogin by lazy {
        loginStatusLiveDataM.liveDataDistinct
            .map { it.hasLogin }
            .distinctUntilChanged()
    }

    init {
        getInitToken()
    }

    private fun getInitToken() {
        viewModelScope.launch {
            when (val savedToken = mmkv.decodeString(MainViewModelKeys.savedToken)) {
                null -> loginStatusLiveDataM.setValue(LoginStatus.NoLogin)
                else -> {
                }
            }
        }
    }

    fun loginByPhoneAndPassword(phone: String, password: String) {
        if (!RegexUtils.isMobileExact(phone)) {
            loginErrorMessageM.postValue(StringUtils.getString(R.string.string_phone_regex_fail))
        } else if (!RegexUtil.isLoginPassword(password)) {
            loginErrorMessageM.postValue(StringUtils.getString(R.string.string_login_password_regex_fail))
        } else {
            viewModelScope.launch {
                userRepository.loginByPhoneAndPassword(phone, password)
                    .dealBusy(loginStatusLiveDataM::setValue) { isBusy ->
                        LoginStatus.LoginPosting.takeIf { isBusy }
                    }
                    .dealError(loginErrorMessageM)
                    .collectLatest {
                        loginStatusLiveDataM.setValue(LoginStatus.LoginIn(it))
                    }
            }
        }
    }
}