package son.ysy.memory.dialog

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import razerdp.basepopup.BaseLazyPopupWindow
import son.ysy.memory.R
import son.ysy.memory.common.base.BaseLazyDialog
import son.ysy.memory.databinding.DialogLoginBinding
import son.ysy.memory.ui.activity.MainViewModel
import son.ysy.memory.ui.splash.SplashViewModel

class LoginDialog(
    fragment: Fragment,
    private val bgWidth: Int,
    private val bgHeight: Int,
    fullWidth: Int
) : BaseLazyDialog(fragment, (fullWidth * 0.9).toInt(), 0) {


    private lateinit var cancelJob: Job

    private val viewModel: SplashViewModel by fragment.viewModel()


    override fun onBindView(contentView: View) {
        resetBgSize(bgWidth, bgHeight)

        bindLifecycleOwner(fragment)

        setBackgroundColor(Color.TRANSPARENT)

        setAdjustInputMethod(true)

        bindViewModel()

        setOnPopupWindowShowListener {
            setOnPopupWindowShowListener(null)
            GlobalScope.launch(SupervisorJob().apply { cancelJob = this } + Dispatchers.Main) {
                binding.mlLoginDialog.apply {
                    transitionToState(R.id.middle)
                    awaitTransitionComplete()
                    transitionToState(R.id.end)
                    awaitTransitionComplete()
                    if (binding.etLoginDialogPhone.text.isNullOrBlank()) {
                        KeyboardUtils.showSoftInput(binding.etLoginDialogPhone)
                    } else if (binding.etLoginDialogPassword.text.isNullOrBlank()) {
                        KeyboardUtils.showSoftInput(binding.etLoginDialogPassword)
                    }
                }
            }
        }

        binding.btnLoginDialogContinue.setOnClickListener {
            arrayOf(
                binding.etLoginDialogPhone,
                binding.etLoginDialogPassword
            ).firstOrNull { it.hasFocus() }
                ?.apply(KeyboardUtils::hideSoftInput)

            viewModel.loginByPhoneAndPassword(
                binding.etLoginDialogPhone.text.toString(),
                binding.etLoginDialogPassword.text.toString()
            )
        }
    }

    private fun bindViewModel() {
        viewModel.loginStatus
            .observe { loginStatus ->
                binding.btnLoginDialogContinue.isClickable =
                    !(loginStatus.isBusy || loginStatus.hasLogin)
                when {
                    loginStatus.isBusy -> {
                        binding.btnLoginDialogContinue.setText(R.string.string_common_busy)
                    }
                    loginStatus.hasLogin -> {
                        binding.btnLoginDialogContinue.setText(R.string.string_splash_login_success)
                        onBackPressed()
                    }
                    else -> {
                        binding.btnLoginDialogContinue.setText(R.string.string_splash_login_continue)
                    }
                }
            }
        viewModel.loginErrorMessage?.observe {
            ToastUtils.showLong(it)
        }
    }

    private fun resetBgSize(width: Int, height: Int) {
        binding.mlLoginDialog.apply {
            val constraintSet = getConstraintSet(R.id.start)
            constraintSet.constrainWidth(R.id.bgLoginDialog, width)
            constraintSet.constrainHeight(R.id.bgLoginDialog, height)
            updateState(R.id.start, constraintSet)
        }
    }

    override fun onDismiss() {
        super.onDismiss()
        cancelJob.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelJob.cancel()
    }

    override fun onBackPressed(): Boolean {
        if (viewModel.loginStatus.value?.isBusy != true) {
            GlobalScope.launch(cancelJob + Dispatchers.Main) {
                binding.mlLoginDialog.apply {
                    transitionToState(R.id.middle)
                    awaitTransitionComplete()
                    transitionToState(R.id.start)
                    awaitTransitionComplete()
                    dismiss(false)
                }
            }
        }
        return true
    }

    override fun onOutSideTouch(): Boolean {
        return onBackPressed()
    }
}