package son.ysy.memory.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass

abstract class BaseFragment<Binding : ViewBinding>(
    private val bindingClz: KClass<Binding>
) : Fragment() {

    protected var binding: Binding? = null

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = (bindingClz.java
        .getDeclaredMethod("inflate", LayoutInflater::class.java)
        .invoke(null, layoutInflater) as Binding)
        .apply {
            binding = this
        }
        .root

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply(::onBindView)
    }


    protected open fun onBindView(binding: Binding) {

    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    protected fun <T : Any> LiveData<T>.observe(observer: Observer<T>) {
        observe(viewLifecycleOwner, observer)
    }
}