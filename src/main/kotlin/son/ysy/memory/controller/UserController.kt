package son.ysy.memory.controller

import kotlinx.coroutines.reactor.mono
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import son.ysy.memory.entity.LoginResponseParam
import son.ysy.memory.service.UserService

@RestController
@RequestMapping("user")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @PostMapping("login")
    fun postLogin(@RequestBody loginParam: LoginResponseParam) = mono {
        userService.loginByPhoneAndPassword(this, loginParam.phone, loginParam.password)
    }

    @GetMapping("marker")
    fun getUserMarker(phone: String) = mono {
        userService.getMarkerByPhone(this, phone)
    }
}