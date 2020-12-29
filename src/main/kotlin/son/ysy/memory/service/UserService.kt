package son.ysy.memory.service

import kotlinx.coroutines.CoroutineScope
import org.apache.ibatis.annotations.Select
import son.ysy.memory.entity.UserId

interface UserService {
    /**
     *通过手机号+密码获取登录凭证
     * @param phone 手机号
     * @param password 密码
     * @return 登录凭证
     */
    fun getNewToken(scope: CoroutineScope, phone: String, password: String): String

    /**
     * 通过手机号返回称呼
     * @param id 用户id
     * @return 称呼
     */
    fun getMarkerByPhone(scope: CoroutineScope, id: String): String
}