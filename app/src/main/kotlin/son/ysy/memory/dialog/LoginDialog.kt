package son.ysy.memory.dialog

import android.graphics.Color
import android.view.View
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.KeyboardUtils
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import razerdp.basepopup.BaseLazyPopupWindow
import son.ysy.memory.R
import son.ysy.memory.databinding.DialogLoginBinding
import son.ysy.memory.ui.activity.MainViewModel

class LoginDialog(
    private val fragment: Fragment,
    private val bgWidth: Int,
    private val bgHeight: Int
) : BaseLazyPopupWindow(fragment) {

    private lateinit var binding: DialogLoginBinding

    private lateinit var cancelJob: Job

    private val mainViewModel: MainViewModel by fragment.sharedViewModel()

    override fun onCreateContentView(): View {
        binding = DialogLoginBinding.bind(createPopupById(R.layout.dialog_login))
        return binding.root
    }

    override fun onViewCreated(contentView: View) {
        super.onViewCreated(contentView)

        resetBgSize(bgWidth, bgHeight)

        bindLifecycleOwner(fragment)

        setBackgroundColor(Color.TRANSPARENT)

        setAdjustInputMethod(true)

        setOnPopupWindowShowListener {
            setOnPopupWindowShowListener(null)
            GlobalScope.launch(SupervisorJob().apply { cancelJob = this } + Dispatchers.Main) {
                binding.mlLoginDialog.apply {
                    transitionToState(R.id.middle)
                    awaitTransitionComplete()
                    transitionToState(R.id.end)
                    awaitTransitionComplete()
                    KeyboardUtils.showSoftInput(binding.etLoginDialogPhone)
                }
            }
        }

        binding.btnLoginDialogContinue.setOnClickListener {
            arrayOf(
                binding.etLoginDialogPhone,
                binding.etLoginDialogPassword
            ).firstOrNull { it.hasFocus() }
                ?.apply(KeyboardUtils::hideSoftInput)

            mainViewModel.loginByPhoneAndPassword(
                binding.etLoginDialogPhone.text.toString(),
                binding.etLoginDialogPassword.text.toString()
            )
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
        GlobalScope.launch(cancelJob + Dispatchers.Main) {
            binding.mlLoginDialog.apply {
                transitionToState(R.id.middle)
                awaitTransitionComplete()
                transitionToState(R.id.start)
                awaitTransitionComplete()
                dismiss(false)
            }
        }
        return true
    }

    override fun onOutSideTouch(): Boolean {
        return onBackPressed()
    }
}