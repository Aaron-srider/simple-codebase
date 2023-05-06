package fit.wenchao.simplecodebase.dao.po

import fit.wenchao.simplecodebase.dao.repo.base.BaseDao
import fit.wenchao.simplecodebase.dao.repo.base.pagination.Condition
import fit.wenchao.simplecodebase.dao.repo.base.pagination.PageConfig
import fit.wenchao.simplecodebase.dao.repo.base.pagination.PageNo
import fit.wenchao.simplecodebase.dao.repo.base.pagination.PageSize
import fit.wenchao.simplecodebase.exception.BackendException
import fit.wenchao.simplecodebase.exception.RespCode
import fit.wenchao.simplecodebase.model.JsonResult
import fit.wenchao.simplecodebase.service.SnippetsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
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

@PageConfig
class QuerySnippet {
    @PageSize
    var pageSize: Long? = null

    @PageNo
    var pageNo: Long? = null

    @Condition(dbFieldName = "lang", con = BaseDao.ConditionEnum.EQ)
    var lang: String? = null

    @Condition(dbFieldName = "title", con = BaseDao.ConditionEnum.RIGHT_LIKE)
    var title: String? = null
}

class Test {
    var adds = mutableListOf<String>()
}

@Validated
@RestController
@RequestMapping("/API")
class CodeBaseController {

    @Autowired
    lateinit var snippetsService: SnippetsService

    @GetMapping("/snippets")
    fun getAllSnippets(querySnippet: QuerySnippet): JsonResult {
        val page = snippetsService.findAll(querySnippet)

        return JsonResult.ok(object {
            var records = page.records
            var total = page.total
        })
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
    fun createSnippet(@RequestBody @Validated snippet: CreateSnippet): JsonResult {
        snippetsService.create(snippet)
        return JsonResult.ok()
    }


    @PutMapping("/snippets/{id}")
    fun updateSnippet(@PathVariable id: Long, @RequestBody @Validated snippet: UpdateSnippet): JsonResult {
        snippetsService.save(id, snippet)
        return JsonResult.ok()
    }

    @DeleteMapping("/snippets/{id}")
    fun deleteSnippet(@PathVariable id: Long): JsonResult {
        snippetsService.removeSnippetById(id);
        return JsonResult.ok()
    }


}

