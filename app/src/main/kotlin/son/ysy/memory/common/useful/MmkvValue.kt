package son.ysy.memory.common.useful

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.tencent.mmkv.MMKV
import org.koin.core.Koin
import son.ysy.memory.common.error.UnsupportedClassTypeError
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class MmkvValue<T : Any>(
    private val clz: KClass<T>,
    private val key: String,
    private val koin: Koin
) : ReadWriteProperty<Any?, T?> {

    private val unsupportedClassMessage by lazy {
        "不支持类型为:${clz.qualifiedName}的数据!!"
    }

    private val mmkv: MMKV by koin.inject()

    private val jsonAdapter: JsonAdapter<T> by lazy {
        try {
            koin.get<Moshi>().adapter(clz.java)
        } catch (ex: Exception) {
            throw UnsupportedClassTypeError(unsupportedClassMessage)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private var value: T? = when (clz) {
        Boolean::class -> mmkv.decodeBool(key) as T?
        ByteArray::class -> mmkv.decodeBytes(key) as T?
        Double::class -> mmkv.decodeDouble(key) as T?
        Float::class -> mmkv.decodeFloat(key) as T?
        Int::class -> mmkv.decodeInt(key) as T?
        Long::class -> mmkv.decodeLong(key) as T?
        String::class -> mmkv.decodeString(key) as T?
        else -> mmkv.decodeString(key)?.run(jsonAdapter::fromJson)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        this.value = value
        if (value == null) {
            mmkv.remove(key)
            return
        }
        when (clz) {
            Boolean::class -> mmkv.encode(key, value as Boolean)
            ByteArray::class -> mmkv.encode(key, value as ByteArray)
            Double::class -> mmkv.encode(key, value as Double)
            Float::class -> mmkv.encode(key, value as Float)
            Int::class -> mmkv.encode(key, value as Int)
            Long::class -> mmkv.encode(key, value as Long)
            String::class -> mmkv.encode(key, value as String)
            else -> mmkv.encode(key, value.run(jsonAdapter::toJson))
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? = value
}