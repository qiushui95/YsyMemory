package son.ysy.memory.config

import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.GsonHttpMessageConverter

@Configuration
class JsonConfig {

    @Bean
    fun gsonConverter(): HttpMessageConverters {
        val converter = GsonHttpMessageConverter()
        return HttpMessageConverters(true, listOf(converter))
    }
}