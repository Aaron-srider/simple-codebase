package fit.wenchao.simplecodebase.config

import fit.wenchao.simplecodebase.model.JsonResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.lang.Nullable
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

fun main() {
    println(JsonResult.javaClass)
    println(JsonResult().javaClass)
    println(JsonResult()::class.java)
    println(JsonResult::class.java)
}




interface RespBodyWrapper {

    fun wrap(body: Any?): Any

    fun need2Wrap(returnType: MethodParameter): Boolean
}

@Component
class JsonResultRespBodyWrapper : RespBodyWrapper {
    override fun wrap(body: Any?): Any {
        return JsonResult.ok(body)
    }

    override fun need2Wrap(returnType: MethodParameter): Boolean {
        return returnType.parameterType != JsonResult::class.java
    }
}

@ControllerAdvice
class ResponseWrapperAdvice : ResponseBodyAdvice<Any?> {

    @Autowired
    @Qualifier("jsonResultRespBodyWrapper")
    lateinit var respWrapper: RespBodyWrapper

    override fun supports(
        returnType: org.springframework.core.MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>,
    ): Boolean {
        return true // Return true to enable the advice for all controller methods
    }

    override fun beforeBodyWrite(
        @Nullable body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
    ): Any? {
        if (respWrapper.need2Wrap(returnType)) {
            return respWrapper.wrap(body)
        }
        return body
    }
}
