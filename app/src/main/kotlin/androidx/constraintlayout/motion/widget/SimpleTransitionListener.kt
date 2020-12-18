package androidx.constraintlayout.motion.widget

open class SimpleTransitionListener : MotionLayout.TransitionListener {
    override fun onTransitionStarted(motionLayout: MotionLayout, startId: Int, endId: Int) {}

    override fun onTransitionChange(
        motionLayout: MotionLayout,
        startId: Int,
        endId: Int,
        progress: Float
    ) {
    }

    override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {

    }

    override fun onTransitionTrigger(
        motionLayout: MotionLayout,
        triggerId: Int,
        endId: Boolean,
        progress: Float
    ) {

    }
}