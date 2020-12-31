package son.ysy.memory.common.base

import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.kunminx.architecture.ui.callback.ProtectedUnPeekLiveData
import razerdp.basepopup.BaseLazyPopupWindow
import son.ysy.memory.R
import son.ysy.memory.databinding.DialogLoginBinding
import kotlin.reflect.KClass

abstract class BaseLazyDialog(
    protected val fragment: Fragment,
    width: Int,
    height: Int
) : BaseLazyPopupWindow(fragment, width, height) {

    private data class LiveDataWithObserver<T>(
        val liveData: LiveData<T>,
        val observer: Observer<T>
    ) {
        fun observe(fragment: Fragment) {
            if (liveData is ProtectedUnPeekLiveData) {
                liveData.observeInFragment(fragment, observer)
            } else {
                liveData.observe(fragment, observer)
            }
        }

        fun removeObserver() {
            liveData.removeObserver(observer)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as LiveDataWithObserver<*>

            if (liveData != other.liveData) return false

            return true
        }

        override fun hashCode(): Int {
            return liveData.hashCode()
        }
    }

    constructor(fragment: Fragment) : this(fragment, 0, 0)

    private val liveDataObservers by lazy {
        mutableSetOf<LiveDataWithObserver<*>>()
    }

    protected val binding by lazy {
        DialogLoginBinding.bind(createPopupById(R.layout.dialog_login))
    }

    override fun onCreateContentView(): View = binding.root

    final override fun onViewCreated(contentView: View) {
        onBindView(contentView)
    }

    abstract fun onBindView(contentView: View)

    protected fun <T : Any> LiveData<T>.observe(observer: Observer<T>): Observer<T> {
        val liveDataWithObserver = LiveDataWithObserver(this, observer)
        liveDataObservers.asSequence()
            .filter { it == liveDataWithObserver }
            .forEach {
                it.removeObserver()
            }
        liveDataWithObserver.observe(fragment)
        liveDataObservers.add(liveDataWithObserver)
        return observer
    }

    @CallSuper
    override fun onBeforeShow(): Boolean {
        liveDataObservers.forEach {
            it.observe(fragment)
        }
        return super.onBeforeShow()
    }

    @CallSuper
    override fun onBeforeDismiss(): Boolean {
        liveDataObservers.forEach {
            it.removeObserver()
        }
        return super.onBeforeDismiss()
    }
}