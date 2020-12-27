package son.ysy.memory.http

import com.squareup.moshi.Moshi
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import son.ysy.memory.json.ResponseResult
import son.ysy.memory.json.SkipResponseResult
import java.lang.reflect.Type
import kotlin.reflect.full.findAnnotation

@ResponseResult
class MoshiConverterFactory(moshi: Moshi) : Converter.Factory() {

    private val delegateConverterFactory = MoshiConverterFactory.create(moshi)

    private val defaultResponseResultAnnotation = this::class.findAnnotation<ResponseResult>()

    @Suppress("unused")
    fun asLenient(): MoshiConverterFactory = delegateConverterFactory.asLenient()

    @Suppress("unused")
    fun failOnUnknown(): MoshiConverterFactory = delegateConverterFactory.failOnUnknown()

    @Suppress("unused")
    fun withNullSerialization(): MoshiConverterFactory {
        return delegateConverterFactory.withNullSerialization()
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val annotationList = annotations.toList()

        val containsSkip = annotationList.any { it is SkipResponseResult }
        val containsResult = annotationList.any { it is ResponseResult }

        val resultAnnotationList = when {
            containsSkip -> annotationList.filterNot { it is SkipResponseResult }
            containsResult -> annotationList
            else -> annotationList.plus(defaultResponseResultAnnotation!!)
        }

        val resultAnnotationArray = Array(resultAnnotationList.size) {
            resultAnnotationList[it]
        }

        return delegateConverterFactory.responseBodyConverter(type, resultAnnotationArray, retrofit)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        return delegateConverterFactory.requestBodyConverter(
            type,
            parameterAnnotations,
            methodAnnotations,
            retrofit
        )
    }
}