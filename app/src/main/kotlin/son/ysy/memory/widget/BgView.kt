package son.ysy.memory.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.util.AttributeSet
import android.view.View
import androidx.annotation.FloatRange
import kotlin.math.min

class BgView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    override fun setBackground(background: Drawable?) {
        val gradientDrawable = when (background) {
            is ColorDrawable -> {
                GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    setColor(background.color)
                }
            }
            is GradientDrawable -> {
                background
            }
            else -> {
                GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    setColor(Color.TRANSPARENT)
                }
            }
        }
        super.setBackground(gradientDrawable)
    }

    @Suppress("unused")
    fun setCornerRadiusRate(
        @FloatRange(
            from = 0.0,
            fromInclusive = true,
            to = 1.0,
            toInclusive = true
        ) rate: Float
    ) {
        val oldBackground = background
        if (oldBackground is GradientDrawable) {
            oldBackground.cornerRadius = min(width, height) / 2f * rate
        }
    }
}