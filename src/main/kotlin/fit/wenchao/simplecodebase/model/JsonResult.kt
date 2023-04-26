package fit.wenchao.simplecodebase.model

import fit.wenchao.simplecodebase.consts.RespCode

data class JsonResult (
    var data: Any?,
    var code: String?,
    var msg: String?
) {

    // default constructor
    constructor() : this(null, null, null)

    companion object {
        fun of(data: Any?, respCode: RespCode): JsonResult {
            return JsonResult(data, respCode.name, respCode.msg)
        }

        fun ok(): JsonResult {
            return JsonResult(null, RespCode.SUCCESS.name, RespCode.SUCCESS.msg)
        }

        fun ok(data: Any?): JsonResult {
            return JsonResult(data, RespCode.SUCCESS.name, RespCode.SUCCESS.msg)
        }
    }



}