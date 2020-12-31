package son.ysy.memory.ui.splash

import androidx.annotation.IdRes
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.SimpleTransitionListener
import androidx.core.view.doOnLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.StringUtils
import kotlinx.coroutines.delay
import org.joda.time.DateTime
import org.koin.androidx.viewmodel.ext.android.viewModel
import razerdp.basepopup.BasePopupWindow
import son.ysy.memory.R
import son.ysy.memory.common.base.BaseFragment
import son.ysy.memory.databinding.FragmentSplashBinding
import son.ysy.memory.dialog.LoginDialog
import son.ysy.memory.common.entity.LoginStatus
import kotlin.math.min

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::class) {

    private val viewModel by viewModel<SplashViewModel>()

    private var markObserver: Observer<String>? = null

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
        viewModel.loginStatus
            .observe { loginStatus ->
                val binding = binding ?: return@observe
                when {
                    !loginStatus.hasLogin -> {
                        binding.tvSplashWelcome.text = getString(
                            R.string.string_splash_login_tip,
                            getTimeDesc()
                        )
                        binding.tvSplashButtonText.setText(R.string.string_splash_login)
                        binding.clickAreaSplash.setOnClickListener {
                            viewModel.onStateChanged(R.id.middle)
                        }

                        markObserver?.apply {
                            viewModel.markerRequestGetter.dataLiveDataDistinct?.removeObserver(this)
                        }
                    }
                    loginStatus is LoginStatus.LoginIn -> {
                        binding.clickAreaSplash.setOnClickListener(null)
                        binding.tvSplashButtonText.setText(R.string.string_common_busy)
                        viewModel.onTokenChanged()
                        markObserver = viewModel.markerRequestGetter.dataLiveDataDistinct?.observe {
                            binding.tvSplashWelcome.text = getString(
                                R.string.string_splash_welcome,
                                getTimeDesc(),
                                it
                            )
                            binding.clickAreaSplash.setOnClickListener {
                                viewModel.onStateChanged(R.id.end)
                            }

                            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                                repeat(3) { second ->
                                    binding.tvSplashButtonText.text = StringUtils.getString(
                                        R.string.string_splash_enter_format,
                                        3-second
                                    )
                                    delay(1000)
                                }
                                binding.clickAreaSplash.performClick()
                            }
                        }
                    }
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
                            binding.bgSplash.height,
                            motionLayout.width
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