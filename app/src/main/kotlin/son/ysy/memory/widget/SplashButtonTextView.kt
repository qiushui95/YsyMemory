package son.ysy.memory.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.blankj.utilcode.util.StringUtils
import son.ysy.memory.R

class SplashButtonTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    fun showEnterText() {
        setText(R.string.string_splash_enter)
    }

    fun showEnterText(second: Int) {
        text = StringUtils.getString(R.string.string_splash_enter_format, second)
    }

    fun showLoginText() {
        setText(R.string.string_splash_login)
    }

    fun showLoginContinueText() {
        setText(R.string.string_splash_login_continue)
    }
}