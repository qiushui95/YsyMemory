package son.ysy.memory.service.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.converter.json.GsonHttpMessageConverter
import org.springframework.stereotype.Service
import org.springframework.util.DigestUtils
import son.ysy.memory.error.UserNotRegisterError
import son.ysy.memory.error.UserPasswordIncorrectError
import son.ysy.memory.mapper.UserMapper
import son.ysy.memory.service.UserService
import son.ysy.memory.util.TokenUtils

@Service
class UserServiceImpl : UserService {

    @Autowired
    private lateinit var userMapper: UserMapper

    @Autowired
    private lateinit var redis: StringRedisTemplate

    override fun loginByPhoneAndPassword(scope: CoroutineScope, phone: String, password: String): String {
        return when {
            userMapper.checkUserByPhoneAndPassword(phone, password) -> {
                val newToken = DigestUtils.md5DigestAsHex("$phone${System.currentTimeMillis()}".toByteArray())
                scope.launch(Dispatchers.IO) {
                    saveNewToken(phone, newToken)
                }
                newToken
            }
            userMapper.checkUserRegistered(phone) -> {
                throw UserPasswordIncorrectError()
            }
            else -> {
                throw UserNotRegisterError()
            }
        }
    }

    override fun getMarkerByPhone(scope: CoroutineScope, phone: String): String {
        return userMapper.getMarkerByPhone(phone) ?: throw UserNotRegisterError()
    }

    private fun saveNewToken(phone: String, newToken: String) {
        val phoneKey = TokenUtils.createPhoneKey(phone)
        val valueOperation = redis.opsForValue()
        if (redis.hasKey(phoneKey)) {
            valueOperation[phoneKey]
                ?.run(TokenUtils::createTokenKey)
                ?.apply(redis::delete)
        }
        valueOperation[phoneKey] = newToken
        valueOperation[TokenUtils.createTokenKey(newToken)] = phone
    }
}