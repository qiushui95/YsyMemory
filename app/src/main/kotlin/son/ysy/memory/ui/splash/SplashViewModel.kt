package son.ysy.memory.ui.splash

import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.distinctUntilChanged
import son.ysy.memory.R
import son.ysy.memory.base.BaseViewModel

class SplashViewModel(handle: SavedStateHandle) : BaseViewModel() {
    private object Keys {
        const val KEY_STATE_ID = "stateId"
    }

    private val stateIdM = handle.getLiveData(Keys.KEY_STATE_ID, R.id.start)

    val stateId: LiveData<Int> by lazy {
        stateIdM.distinctUntilChanged()
    }

    fun onStateChanged(@IdRes stateId: Int) {
        stateIdM.value = stateId
    }
}