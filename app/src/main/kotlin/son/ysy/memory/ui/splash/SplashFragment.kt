package son.ysy.memory.ui.splash

import androidx.annotation.IdRes
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.SimpleTransitionListener
import androidx.core.view.doOnLayout
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import org.joda.time.DateTime
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import razerdp.basepopup.BasePopupWindow
import son.ysy.memory.R
import son.ysy.memory.base.BaseFragment
import son.ysy.memory.databinding.FragmentSplashBinding
import son.ysy.memory.dialog.LoginDialog
import son.ysy.memory.ui.activity.MainViewModel
import kotlin.math.min

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::class) {

    private val mainViewModel by sharedViewModel<MainViewModel>()
    private val viewModel by viewModel<SplashViewModel>()

    override fun onBindView(binding: FragmentSplashBinding) {
        BarUtils.setNavBarVisibility(requireActivity(), false)
        resetAvatarSize(R.id.start, R.id.end, R.id.middle)
        bindViewModel()
        bindMotionListener()
    }

    private fun resetAvatarSize(vararg stateIds: Int) {
        binding?.root?.doOnLayout {
            val root = binding?.root ?: return@doOnLayout
            val avatarPercent = min(
                root.width,
                root.height
            ) / 2f / root.width

            stateIds.forEach {
                updateAvatarConstrain(avatarPercent, it)
            }
        }
    }

    private fun updateAvatarConstrain(avatarPercent: Float, @IdRes stateId: Int) {
        val mlSplash = binding?.mlSplash ?: return
        val constraintSet = mlSplash.getConstraintSet(stateId)
        constraintSet.constrainPercentWidth(R.id.ivSplashAvatar, avatarPercent)
        mlSplash.updateState(stateId, constraintSet)
    }

    private fun bindViewModel() {
        mainViewModel.hasLogin
            .observe {
                val binding = binding ?: return@observe
                if (!it) {
                    binding.tvSplashWelcome.text = getString(
                        R.string.string_splash_login_tip,
                        getTimeDesc()
                    )
                    binding.tvSplashButtonText.setText(R.string.string_splash_login)
                    binding.clickAreaSplash.setOnClickListener {
                        binding.mlSplash.transitionToState(R.id.middle)
                    }
                } else {

                }
            }
        viewModel.stateId
            .observe {
                binding?.mlSplash?.transitionToState(it)
            }
    }


    private fun bindMotionListener() {
        binding?.mlSplash?.addTransitionListener(object : SimpleTransitionListener() {
            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                val binding = binding ?: return
                when (currentId) {
                    R.id.middle -> {
                        LoginDialog(
                            this@SplashFragment,
                            binding.bgSplash.width,
                            binding.bgSplash.height
                        ).setOnDismissListener(object : BasePopupWindow.OnDismissListener() {
                            override fun onDismiss() {
                                binding.mlSplash.transitionToState(R.id.start)
                            }
                        }).showPopupWindow()
                    }
                }
            }
        })
    }

    private fun getTimeDesc() = when (DateTime.now().hourOfDay) {
        in 0..5 -> "凌晨好"
        in 6..7 -> "早上好"
        in 8..10 -> "上午好"
        in 11..13 -> "中午好"
        in 14..17 -> "下午好"
        in 18..19 -> "傍晚好"
        else -> "晚上好"
    }
}