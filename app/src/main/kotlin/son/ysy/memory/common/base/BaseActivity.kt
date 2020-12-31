package son.ysy.memory.common.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.kunminx.architecture.ui.callback.ProtectedUnPeekLiveData
import son.ysy.memory.common.error.UnsupportedClassTypeError
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

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

    @MainThread
    protected inline fun <reified T : Any> LiveData<T>.observe(observer: Observer<T>): Observer<T> {
         if (this is ProtectedUnPeekLiveData) {
            observeInActivity(this@BaseActivity, observer)
        } else {
            observe(this@BaseActivity, observer)
        }
        return observer
    }
}