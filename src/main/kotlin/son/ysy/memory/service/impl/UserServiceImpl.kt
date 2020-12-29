package son.ysy.memory.service.impl

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.util.DigestUtils
import son.ysy.memory.entity.UserId
import son.ysy.memory.error.UserNotRegisterError
import son.ysy.memory.error.UserPasswordIncorrectError
import son.ysy.memory.mapper.UserMapper
import son.ysy.memory.service.UserService
import son.ysy.memory.util.RedisKeyUtil

@Service
class UserServiceImpl : UserService {

    @Autowired
    private lateinit var userMapper: UserMapper

    @Autowired
    private lateinit var redis: StringRedisTemplate

    override fun getNewToken(scope: CoroutineScope, phone: String, password: String): String {
        val userId = userMapper.getIdByPhoneAndPassword(phone, password)
        return when {
            userId != null -> {
                val newToken = DigestUtils.md5DigestAsHex("$phone${System.currentTimeMillis()}".toByteArray())
                scope.launch(Dispatchers.IO) {
                    saveNewToken(userId, newToken)
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

    override fun getMarkerByPhone(scope: CoroutineScope, id: String): String {
        return userMapper.getMarkerById(id) ?: throw UserNotRegisterError()
    }

    private fun saveNewToken(id: String, newToken: String) {

        val hashRedis = redis.opsForHash<String, String>()

        hashRedis[id, RedisKeyUtil.KEY_TOKEN]?.apply { hashRedis.delete(RedisKeyUtil.KEY_TOKEN, this) }
        hashRedis.put(RedisKeyUtil.KEY_TOKEN, newToken, id)
        hashRedis.put(id, RedisKeyUtil.KEY_TOKEN, newToken)
    }
}