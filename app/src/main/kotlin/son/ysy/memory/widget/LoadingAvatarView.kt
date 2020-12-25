package son.ysy.memory.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.blankj.utilcode.util.LogUtils
import son.ysy.memory.R
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class LoadingAvatarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val fullRect by lazy {
        RectF()
    }

    private val contentRect by lazy {
        RectF()
    }

    private val imageClearPath by lazy {
        Path()
    }

    private val clearPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.WHITE
            strokeWidth = 0f
            style = Paint.Style.FILL
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
    }

    private val progressPath by lazy {
        Path()
    }

    private val progressPaint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            strokeWidth = 0f
            isAntiAlias = true
            color = Color.RED
        }
    }

    private val progressColor: Int
    private val progressPercent: Float
    private val progressSize: Int

    private var isDataDirty = true

    private var isLoading = false

    private var startAngle: Float = 0f
    private var startAngleStep: Int = 5
    private var sweepAngle: Float = 0f
    private var sweepAngleStep: Int = 4


    init {
        context.obtainStyledAttributes(attrs, R.styleable.LoadingAvatarView).apply {
            progressColor = getColor(R.styleable.LoadingAvatarView_progressColor, Color.WHITE)
            progressPercent = getFloat(R.styleable.LoadingAvatarView_progressPercent, 0f).run {
                min(1f, this)
            }.run {
                max(0f, this)
            }
            progressSize = getDimensionPixelOffset(R.styleable.LoadingAvatarView_progressSize, 0)
        }.recycle()
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        if (isDataDirty) {
            resetPath()
            isDataDirty = false
        }
        canvas.saveLayer(contentRect, null)
        super.onDraw(canvas)
        canvas.drawPath(imageClearPath, clearPaint)
        canvas.translate(fullRect.width() / 2f, fullRect.height() / 2f)
        canvas.rotate(180f)
        canvas.translate(fullRect.width() / -2f, fullRect.height() / -2f)
        canvas.drawPath(imageClearPath, clearPaint)
        canvas.restore()

        if (isLoading) {
            val layerId = canvas.saveLayer(contentRect, progressPaint)

            canvas.drawPath(progressPath, progressPaint)
            canvas.drawArc(
                contentRect,
                startAngle,
                360 - sweepAngle,
                true,
                clearPaint
            )
            canvas.restoreToCount(layerId)
        }
        if (isLoading) {
            startAngle = (startAngle + startAngleStep) % 360

            val newSweepAngle = abs(sweepAngle + sweepAngleStep)
            sweepAngle = max(0f, min(360f, newSweepAngle))
            if (newSweepAngle <= 0 || newSweepAngle >= 360) {
                sweepAngleStep *= -1
            }
            invalidate()
        }
    }

    private fun resetPath() {
        fullRect.set(0f, 0f, width * 1f, height * 1f)
        val radius = min(width, height) / 2f
        contentRect.set(
            fullRect.centerX() - radius,
            fullRect.centerY() - radius,
            fullRect.centerX() + radius,
            fullRect.centerY() + radius
        )
        imageClearPath.apply {
            reset()
            moveTo(0f, 0f)
            lineTo(0f, fullRect.centerY())
            arcTo(
                0f,
                fullRect.centerY() - radius,
                fullRect.right,
                fullRect.centerY() + radius,
                180f,
                180f,
                false
            )
            lineTo(fullRect.right, 0f)
            close()
        }
        val progressSize = (progressSize * 1f).takeIf { it > 0 } ?: radius * progressPercent
        progressPath.apply {
            reset()
            fillType = Path.FillType.EVEN_ODD
            val centerX = fullRect.centerX()
            val centerY = fullRect.centerY()
            addCircle(centerX, centerY, radius, Path.Direction.CW)
            addCircle(
                centerX,
                centerY,
                radius - progressSize,
                Path.Direction.CW
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        isDataDirty = true
        super.onSizeChanged(w, h, oldw, oldh)
    }

    fun startLoading() {
        isLoading = true
        postInvalidate()
    }

    fun finishLoading() {
        isLoading = false
    }
}