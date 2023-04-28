package fit.wenchao.simplecodebase.service

import fit.wenchao.simplecodebase.rest.CodeEvaluationRequest


interface CodeEvaluationService {
    fun evaluateCode(request: CodeEvaluationRequest): String
}