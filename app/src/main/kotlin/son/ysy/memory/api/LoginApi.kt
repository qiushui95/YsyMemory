package son.ysy.memory.api

import retrofit2.http.POST
import son.ysy.memory.entity.LoginRequestParam

interface LoginApi {

    @POST("user/login")
    suspend fun loginByPhoneAndPassword(loginRequestParam: LoginRequestParam): String
}