package son.ysy.memory.service

import kotlinx.coroutines.CoroutineScope
import org.apache.ibatis.annotations.Select

interface UserService {
    /**
     *通过手机号+密码登录
     * @param phone 手机号
     * @param password 密码
     * @return 登录凭证
     */
    fun loginByPhoneAndPassword(scope: CoroutineScope, phone: String, password: String): String

    /**
     * 通过手机号返回称呼
     * @param phone 手机号
     * @return 称呼
     */
    fun getMarkerByPhone(scope: CoroutineScope, phone: String): String
}