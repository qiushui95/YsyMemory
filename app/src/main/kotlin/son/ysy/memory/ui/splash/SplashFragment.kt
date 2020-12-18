package son.ysy.memory.ui.splash

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.SimpleTransitionListener
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.net.toUri
import androidx.core.view.doOnLayout
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import org.joda.time.DateTime
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import son.ysy.memory.R
import son.ysy.memory.base.BaseFragment
import son.ysy.memory.databinding.FragmentSplashBinding
import son.ysy.memory.ui.activity.MainViewModel
import kotlin.math.abs
import kotlin.math.min

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::class),
    KeyboardUtils.OnSoftInputChangedListener {

    private val mainViewModel by sharedViewModel<MainViewModel>()
    private val viewModel by viewModel<SplashViewModel>()


    override fun onBindView(binding: FragmentSplashBinding) {
        BarUtils.setNavBarVisibility(requireActivity(), false)
        resetAvatarSize(0, R.id.start, R.id.end, R.id.login, R.id.loginInput)
        bindViewModel()
        bindSoftKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbindSoftKeyboard()
    }

    private fun resetAvatarSize(offset: Int = 0, vararg stateIds: Int) {
        binding?.root?.doOnLayout {
            val root = binding?.root ?: return@doOnLayout
            val avatarPercent = (min(
                root.width,
                root.height
            ) - offset) / 2f / root.width

            stateIds.forEach {
                updateAvatarConstrain(avatarPercent, it)
            }
        }
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
                        binding.mlSplash.transitionToState(R.id.login)
                    }
                } else {

                }
            }
        viewModel.stateId
            .observe {
                binding?.mlSplash?.transitionToState(it)
            }
    }

    private fun bindSoftKeyboard() {
        KeyboardUtils.registerSoftInputChangedListener(requireActivity(), this)
    }

    private fun unbindSoftKeyboard() {
        KeyboardUtils.unregisterSoftInputChangedListener(requireActivity().window)
    }

    private fun updateAvatarConstrain(avatarPercent: Float, @IdRes stateId: Int) {
        val mlSplash = binding?.mlSplash ?: return
        val constraintSet = mlSplash.getConstraintSet(stateId)
        constraintSet.constrainPercentWidth(R.id.ivSplashAvatar, avatarPercent)
        mlSplash.updateState(stateId, constraintSet)
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

    override fun onSoftInputChanged(height: Int) {
        val binding = binding ?: return
        val mlSplash = binding.mlSplash
        if (height > 0 && mlSplash.currentState == R.id.login) {
            if (binding.bgSplash.top < binding.tvSplashWelcome.bottom) {
                resetAvatarSize(100, R.id.loginInput)
            }

            mlSplash.transitionToState(R.id.loginInput)
        } else if (height <= 0 && mlSplash.currentState == R.id.loginInput) {
            mlSplash.transitionToState(R.id.login)
        }
    }
}