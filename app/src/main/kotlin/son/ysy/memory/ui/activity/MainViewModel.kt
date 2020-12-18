package son.ysy.memory.ui.activity

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import com.tencent.mmkv.MMKV
import org.koin.core.Koin
import son.ysy.memory.base.BaseViewModel

class MainViewModel(handle: SavedStateHandle, koin: Koin) : BaseViewModel() {
    private object Keys {
        const val KEY_SAVED_TOKEN = "savedToken"
    }

    private val mmkv by koin.inject<MMKV>()

    private val savedTokenM = handle.getLiveData<String>(
        Keys.KEY_SAVED_TOKEN,
        mmkv.getString(Keys.KEY_SAVED_TOKEN, "")
    )

    val hasLogin by lazy {
        savedTokenM.distinctUntilChanged()
            .map {
                it.isNotBlank()
            }
    }

    fun onTokenChanged(newToken: String?) {
        savedTokenM.value = newToken ?: ""
    }
}