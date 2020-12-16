package son.ysy.memory.entity

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import son.ysy.memory.error.ApiError

@JsonSerialize(using = ResponseDataSerializer::class)
data class ResponseResult<T : Any>(val code: Int, val message: String, val data: T?) {
    object Constants {
        const val MESSAGE_COMMON_SUCCESS = "操作成功"
        const val CODE_COMMON_SUCCESS = 100
    }

    companion object {

        inline fun <reified T : Any> Success(
            code: Int,
            message: String,
            data: T
        ) = ResponseResult(code, message, data)

        inline fun <reified T : Any> Success(
            message: String,
            data: T
        ) = Success(Constants.CODE_COMMON_SUCCESS, message, data)

        inline fun <reified T : Any> Success(
            code: Int,
            data: T
        ) = Success(code, Constants.MESSAGE_COMMON_SUCCESS, data)

        inline fun <reified T : Any> Success(data: T) = Success(Constants.MESSAGE_COMMON_SUCCESS, data)

        fun Error(error: ApiError) = ResponseResult(error.code, error.msg, error.data)
    }

}

class ResponseDataSerializer : JsonSerializer<ResponseResult<*>>() {
    override fun serialize(value: ResponseResult<*>, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeStartObject()
        gen.writeFieldName("code")
        gen.writeNumber(value.code)
        gen.writeFieldName("message")
        gen.writeString(value.message)
        if (value.data != null) {
            if (value.code == 100) {
                "data"
            } else {
                "errorData"
            }.apply(gen::writeFieldName)
            gen.writeObject(value.data)
        }
        gen.writeEndObject()
    }
}
