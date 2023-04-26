package fit.wenchao.simplecodebase.exception

import fit.wenchao.simplecodebase.consts.RespCode

class BackendException : RuntimeException {
    val data: Any?
    val code: String?
    val msg: String?

    constructor(data: Any?, respCode: RespCode) : super(respCode.msg) {
        this.data = data
        this.code = respCode.name
        this.msg = respCode.msg
    }

    constructor(cause: Throwable?, data: Any?, respCode: RespCode) : super(respCode.msg, cause) {
        this.data = data
        this.code = respCode.name
        this.msg = respCode.msg
    }
}