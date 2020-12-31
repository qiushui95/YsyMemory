package son.ysy.memory.ui.activity

import android.os.Bundle
import android.widget.Toast
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
import son.ysy.memory.common.base.BaseActivity
import son.ysy.memory.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::class) {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}