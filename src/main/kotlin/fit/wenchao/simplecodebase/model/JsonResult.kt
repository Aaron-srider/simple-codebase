package fit.wenchao.simplecodebase.model

import fit.wenchao.simplecodebase.exception.RespCode

data class JsonResult(
    var data: Any?,
    var code: String?,
    var msg: String?
) {

    constructor() : this(null, null, null)

    companion object {

        fun of(data: Any?, code: String, msg: String): JsonResult {
            return JsonResult(data, code, msg)
        }

        fun ok(): JsonResult {
            return JsonResult(null, RespCode.SUCCESS.name, RespCode.SUCCESS.msg)
        }

        fun ok(data: Any?): JsonResult {
            return JsonResult(data, RespCode.SUCCESS.name, RespCode.SUCCESS.msg)
        }
    }


}