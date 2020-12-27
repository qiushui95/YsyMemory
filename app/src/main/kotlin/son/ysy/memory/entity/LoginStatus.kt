package son.ysy.memory.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 登录状态
 * @param hasLogin 是否已登录
 * @param isBusy 是否正忙
 */
sealed class LoginStatus(val hasLogin: Boolean, val isBusy: Boolean) : Parcelable {
    /**
     *已登录
     * @param token->登录凭证
     */
    @Parcelize
    data class LoginIn(val token: String) : LoginStatus(true, false)

    /**
     * 登录凭证检查中
     */
    @Parcelize
    object LoginChecking : LoginStatus(false, true)

    /**
     * 登录中
     */
    @Parcelize
    object LoginPosting : LoginStatus(false, true)

    /**
     * 未登录
     */
    @Parcelize
    object NoLogin : LoginStatus(false, false)

    /**
     * 退出登录
     */
    @Parcelize
    object Logout : LoginStatus(false, false)

    /**
     * 登录凭证过期
     */
    @Parcelize
    object LoginExpired : LoginStatus(false, false)
}
