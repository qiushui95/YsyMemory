package son.ysy.memory.common.json

import com.squareup.moshi.JsonQualifier

@JsonQualifier
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class ResponseResult(
    val codeJsonName: String = defaultCodeJsonName,
    val messageJsonName: String = defaultMessageJsonName,
    val dataJsonName: String = defaultDataJsonName
) {
    companion object {
        const val defaultCodeJsonName = "code"
        const val defaultMessageJsonName = "message"
        const val defaultDataJsonName = "data"
    }
}
