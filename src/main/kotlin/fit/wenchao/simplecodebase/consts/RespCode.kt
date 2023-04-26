package fit.wenchao.simplecodebase.consts;

enum class RespCode(var msg: String) {


    SUCCESS("success"),
    FRONT_END_PARAMS_ERROR("front end params error"),
    UPLOAD_FILE_SIZE_EXCEED_UPPER_LIMIT("upload file size exceed upper limit"),
    UPLOAD_FILE_UNKNOWN_ERROR("upload file unknown error"),
    UPLOAD_FILE_MISSING("upload file missing"),
    OTHER_ERROR("other error"),
    NO_SNIPPET("no snippet"),

    ;


}