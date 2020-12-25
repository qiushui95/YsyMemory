package son.ysy.memory.controller

import kotlinx.coroutines.reactor.mono
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import son.ysy.memory.entity.LoginRequestParam
import son.ysy.memory.entity.UserId
import son.ysy.memory.service.UserService

@RestController
@RequestMapping("user")
class UserController : BaseController() {

    @Autowired
    private lateinit var userService: UserService

    @PostMapping("login")
    fun postLogin(@RequestBody loginParam: LoginRequestParam) = mono {
        userService.loginByPhoneAndPassword(this, loginParam.phone, loginParam.password)
    }.toResponseResult()

    @GetMapping("marker")
    fun getUserMarker(userId: UserId) = mono {
        userService.getMarkerByPhone(this, userId.value)
    }.toResponseResult()
}