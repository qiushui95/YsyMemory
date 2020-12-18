package son.ysy.memory.ui.activity

import android.os.Bundle
import com.blankj.utilcode.util.KeyboardUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
import son.ysy.memory.base.BaseActivity
import son.ysy.memory.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::class) {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KeyboardUtils.fixAndroidBug5497(this)
    }
}