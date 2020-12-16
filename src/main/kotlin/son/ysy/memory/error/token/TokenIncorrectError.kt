package son.ysy.memory.error.token

import son.ysy.memory.error.ApiError

class TokenIncorrectError : ApiError(code = CODE, msg = "登录状态过期,请重新登录", data = null) {
    companion object {
        const val CODE = 1001
    }
}