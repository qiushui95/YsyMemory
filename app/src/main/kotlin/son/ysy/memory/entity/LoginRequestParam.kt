package son.ysy.memory.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequestParam(
    @Json(name = "phone") val phone: String,
    @Json(name = "password") val password: String
)