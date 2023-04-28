package fit.wenchao.simplecodebase.service.impl

import fit.wenchao.simplecodebase.rest.CodeEvaluationRequest
import fit.wenchao.simplecodebase.service.CodeEvaluationService
import org.springframework.stereotype.Service
import java.util.*

@Service
class CodeEvaluationServiceImpl: CodeEvaluationService {
    override fun evaluateCode(request: CodeEvaluationRequest): String {
        return when (request.language.lowercase(Locale.getDefault())) {
            "java" -> compileAndRunJava(request.code)
            "kotlin" -> compileAndRunKotlin(request.code)
            else -> throw IllegalArgumentException("Unsupported language")
        }
    }

    private fun compileAndRunJava(code: String): String {
        // Compile Java code and run using JavaCompiler and ProcessBuilder
        return "Hello World!"
    }

    private fun compileAndRunKotlin(code: String): String {
        // Compile Kotlin code and run using KotlinCompiler and ProcessBuilder
        return "Hello World!"
    }
}