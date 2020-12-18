package son.ysy.memory.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass

abstract class BaseActivity<Binding : ViewBinding>(
    private val bindingClz: KClass<Binding>
) : AppCompatActivity() {

    @Suppress("UNCHECKED_CAST")
    protected val binding: Binding by lazy {
        bindingClz.java
            .getDeclaredMethod("inflate", LayoutInflater::class.java)
            .invoke(null, layoutInflater) as Binding
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        onBindView(binding)
    }

    protected open fun onBindView(binding: Binding) {

    }
}