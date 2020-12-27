package son.ysy.memory.json

import com.blankj.utilcode.util.LogUtils
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonAdapter.Factory
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.internal.Util
import son.ysy.memory.error.ResponseResultError

class ResponseResultJsonAdapter<T : Any>(
    moshi: Moshi,
    clz: Class<T>,
    responseResultAnnotation: ResponseResult,
    annotationSet: Set<Annotation>
) : JsonAdapter<T>() {
    companion object {
        val factory = Factory { type, annotations, moshi ->
            val clz = (type as? Class<*>)
            LogUtils.e("type:$type-->annotations:$annotations")

            val responseResult = annotations.filterIsInstance<ResponseResult>()
                .firstOrNull()

            if (clz == null || responseResult == null) {
                null
            } else {
                ResponseResultJsonAdapter(
                    moshi,
                    clz,
                    responseResult,
                    annotations.filterNot {
                        it is ResponseResult || it is SkipResponseResult
                    }.toSet()
                )
            }
        }
    }

    private val annotationArray = annotationSet.run {
        val iterator = iterator()
        Array(size) {
            iterator.next()
        }
    }

    private val containsSkip = Util.isAnnotationPresent(
        annotationSet,
        SkipResponseResult::class.java
    )

    private val dataJsonAdapter = moshi.adapter<T>(clz, annotationSet)

    private val jsonOptions = JsonReader.Options.of(
        responseResultAnnotation.codeJsonName,
        responseResultAnnotation.messageJsonName,
        responseResultAnnotation.dataJsonName
    )

    override fun fromJson(reader: JsonReader): T? {
        if (!containsSkip) {
            val peekJson = reader.peekJson()
            peekJson.beginObject()
            var code: Int? = null
            var message: String? = null

            while (peekJson.hasNext()) {
                when (peekJson.selectName(jsonOptions)) {
                    0 -> code = peekJson.nextInt()
                    1 -> message = peekJson.nextString()
                    else -> peekJson.skipValue()

                }
            }
            peekJson.close()

            when {
                code == null -> throw Util.unexpectedNull("", jsonOptions.strings()[0], reader)
                code != 100 && message != null -> throw ResponseResultError(message)
                code != 100 -> throw ResponseResultError("未知服务器异常,状态码:$code")
            }
        }
        return dataFromJson(reader)
    }

    private fun dataFromJson(reader: JsonReader): T? {
        reader.beginObject()
        var data: T? = null
        while (reader.hasNext()) {
            when (reader.selectName(jsonOptions)) {
                2 -> data = dataJsonAdapter.fromJson(reader)
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return if (Util.hasNullable(annotationArray)) {
            data
        } else {
            data ?: throw Util.unexpectedNull("", jsonOptions.strings()[2], reader)
        }
    }

    override fun toJson(writer: JsonWriter, value: T?) {
        dataJsonAdapter.toJson(writer, value)
    }
}