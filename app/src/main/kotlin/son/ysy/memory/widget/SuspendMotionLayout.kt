package son.ysy.memory.widget

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.SimpleTransitionListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout

class SuspendMotionLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr) {

    suspend fun awaitTransitionComplete(timeout: Long = 10000L) {
        var listener: TransitionListener? = null
        try {
            withTimeout(timeout) {
                suspendCancellableCoroutine<Unit> { continuation ->
                    listener = object : SimpleTransitionListener() {
                        override fun onTransitionCompleted(
                            motionLayout: MotionLayout,
                            currentId: Int
                        ) {
                            apply(::removeTransitionListener)
                            continuation.resume(Unit, null)
                        }
                    }
                    continuation.invokeOnCancellation {
                        listener?.apply(this@SuspendMotionLayout::removeTransitionListener)
                    }
                    addTransitionListener(listener)
                }
            }
        } catch (ex: Exception) {
            listener?.apply(this::removeTransitionListener)
        }
    }
}