package son.ysy.memory.ui.activity

import androidx.lifecycle.SavedStateHandle
import org.koin.core.Koin
import son.ysy.creator.annotations.KeyCreator
import son.ysy.memory.common.base.BaseViewModel

@KeyCreator(keys = ["savedToken"])
class MainViewModel(handle: SavedStateHandle, koin: Koin) : BaseViewModel() {

}