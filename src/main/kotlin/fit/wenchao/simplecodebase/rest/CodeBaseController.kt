package fit.wenchao.simplecodebase.dao.po

import fit.wenchao.simplecodebase.consts.RespCode
import fit.wenchao.simplecodebase.exception.BackendException
import fit.wenchao.simplecodebase.model.JsonResult
import fit.wenchao.simplecodebase.service.SnippetsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotNull

class CreateSnippet {
    @NotNull
    var codeContent: String? = null
    var lang: String? = "java"
    @NotNull
    var title: String? = null
    var description: String? = null
}



class UpdateSnippet {
    var codeContent: String? = null
    var lang: String? = "java"
    var title: String? = null
    var description: String? = null
}

@Validated
@RestController
@RequestMapping("/API")
class CodeBaseController {

    @Autowired
    lateinit var snippetsService: SnippetsService

    @GetMapping("/snippets")
    fun getAllSnippets(): JsonResult {
        return JsonResult.ok(snippetsService.findAll())
    }

    @GetMapping("/snippets/{id}")
    fun getSnippetById(@PathVariable id: Long): JsonResult {
        val snippet = snippetsService.findById(id)
        snippet?.let {
            return JsonResult.ok(snippet)
        } ?: run {
            throw BackendException(null, RespCode.NO_SNIPPET)
        }
    }

    @PostMapping("/snippets")
    fun createSnippet(@RequestBody @Validated snippet: CreateSnippet ): JsonResult {
        snippetsService.save(snippet)
        return JsonResult.ok()
    }


    @PutMapping("/snippets/{id}")
    fun updateSnippet(@PathVariable id: Long, @RequestBody @Validated snippet: UpdateSnippet): JsonResult {
      snippetsService.update(id, snippet)
        return JsonResult.ok()
    }

    @DeleteMapping("/snippets/{id}")
    fun deleteSnippet(@PathVariable id: Long): JsonResult{
        snippetsService.removeSnippetById(id);
        return JsonResult.ok()
    }
}

