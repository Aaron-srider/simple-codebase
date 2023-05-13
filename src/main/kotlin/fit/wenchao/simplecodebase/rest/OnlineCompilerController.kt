package fit.wenchao.simplecodebase.rest

import fit.wenchao.simplecodebase.model.JsonResult
import fit.wenchao.simplecodebase.service.CodeEvaluationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.NotNull


class CodeEvaluationRequest {
    @NotNull
    lateinit var code: String
    @NotNull
    lateinit var language: String
}

@RestController
@RequestMapping("/api/v1/online-compiler")
@Validated
class OnlineCompilerController {

    @Autowired
    lateinit var codeEvaluationService: CodeEvaluationService

    @PostMapping
    fun evaluateCode(@RequestBody @Validated request: CodeEvaluationRequest): ResponseEntity<JsonResult> {
        val result: String = codeEvaluationService.evaluateCode(request)
        return ResponseEntity.ok(JsonResult.ok(object {
            var output = result
        }))
    }
}