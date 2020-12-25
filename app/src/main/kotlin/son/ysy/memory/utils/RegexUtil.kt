package son.ysy.memory.utils

import com.blankj.utilcode.util.RegexUtils

object RegexUtil {

    fun isLoginPassword(password: String) = RegexUtils.isMatch("^[a-z|0-9]{6,16}\$", password)
}