package fit.wenchao.simplecodebase.exception
import com.alibaba.fastjson.JSONObject
import fit.wenchao.simplecodebase.consts.RespCode
import fit.wenchao.simplecodebase.model.JsonResult
import fit.wenchao.simplecodebase.utils.ClassUtils
import fit.wenchao.simplecodebase.utils.Log
import org.apache.commons.lang3.exception.ExceptionUtils
import org.apache.tomcat.util.http.fileupload.FileUploadBase
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.multipart.MultipartException
import javax.servlet.http.HttpServletResponse
import javax.validation.ConstraintViolationException

@ControllerAdvice
class GlobalExceptionHandler: Log() {

    @ExceptionHandler(BackendException::class)
    @ResponseBody
    fun errorCodeException(req: HttpServletResponse, ex: BackendException): JsonResult {
        val jsonResult = JsonResult()
        jsonResult.data = ex.data
        jsonResult.code = ex.code
        var msg = ex.msg
        //if (ex.cause != null) {
        //    msg = buildErrorMsg(ex.cause)
        //}
        jsonResult.msg = msg
        log.error("[{}] {}", ex.code, msg)
        return jsonResult
    }

    @ExceptionHandler(BindException::class, MethodArgumentNotValidException::class)
    @ResponseBody
    fun paramValidateException(ex: Exception): JsonResult {
        var bindingResult: BindingResult? = null
        try {
            bindingResult = ClassUtils.getFieldValue(ex, "bindingResult", BindingResult::class.java)
        } catch (e: NoSuchFieldException) {
            log.error("获取参数校验信息失败")
            return JsonResult.of(null, RespCode.FRONT_END_PARAMS_ERROR)
        }
        val parameterCheckResult = bindingResultPackager(bindingResult)
        val jsonResult = JsonResult.of(parameterCheckResult, RespCode.FRONT_END_PARAMS_ERROR)
        log.error("Error:{}", jsonResult)
        return jsonResult
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseBody
    fun constraintViolationException(ex: ConstraintViolationException): JsonResult {
        val constraintViolations = ex.constraintViolations
        val parameterCheckResult = ParameterCheckResult()
        for (constraintViolation in constraintViolations) {
            parameterCheckResult.putResult(
                getLastPathNode(constraintViolation.propertyPath),
                constraintViolation.message
            )
        }

        return JsonResult.of(parameterCheckResult, RespCode.FRONT_END_PARAMS_ERROR)
    }

    private fun getLastPathNode(path: javax.validation.Path): String {
        val wholePath = path.toString()
        val i = wholePath.lastIndexOf(".")
        return if (i != -1) {
            val substring = wholePath.substring(i + 1, wholePath.length)
            substring
        } else {
            wholePath
        }
    }

    private fun bindingResultPackager(bindingResult: BindingResult): ParameterCheckResult {
        val parameterCheckResult = ParameterCheckResult()
        for (objectError in bindingResult.allErrors) {
            val fieldError = objectError as FieldError
            parameterCheckResult.putResult(fieldError.field, fieldError.defaultMessage?: "")
        }
        return parameterCheckResult
    }

    /**
     * 统一处理其他后端异常
     */
    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun otherException(req: HttpServletResponse, ex: Exception): JsonResult {
        log.error("Server Exception-Name:{}，Server Exception-Msg:{}", ex.javaClass.typeName, ex.message)
        if (ex is HttpMessageNotReadableException || ex is MissingServletRequestParameterException) {
            return errorCodeException(req, BackendException(null, RespCode.FRONT_END_PARAMS_ERROR))
        }
        if (ex is MaxUploadSizeExceededException) {
            return errorCodeException(req, BackendException(null, RespCode.UPLOAD_FILE_SIZE_EXCEED_UPPER_LIMIT))
        }
        if (ex is MultipartException) {
            val rootCause = ExceptionUtils.getRootCause(ex)
            if (rootCause is FileUploadBase.IOFileUploadException) {
                return errorCodeException(req, BackendException(null, RespCode.UPLOAD_FILE_UNKNOWN_ERROR))
            } else if (ex.message?.contains("not a multipart request") == true) {
                return errorCodeException(req, BackendException(null, RespCode.UPLOAD_FILE_MISSING))
            }
        }
        ex.printStackTrace()
        val jsonResult = JsonResult()
        jsonResult.data = null
        jsonResult.code = RespCode.OTHER_ERROR.name
        jsonResult.msg =
            "Server internal error occurred --> [ " + ex.javaClass.typeName + "-->" + ex.message + " ]"
        return jsonResult
    }

}


class ParameterCheckResult {
     private var paramCheckMap = JSONObject()

    fun putResult(field: String, message: String) {
        paramCheckMap[field] = message
    }

    override fun toString(): String {
        return "ParameterCheckResult(paramCheckMap=$paramCheckMap)"
    }

}