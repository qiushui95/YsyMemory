package son.ysy.memory.api

import retrofit2.http.GET

interface UserApi {
    @GET("user/marker")
    suspend fun getMarker(): String
}