package fit.wenchao.simplecodebase.dao.po

import fit.wenchao.simplecodebase.dao.repo.base.BaseDao
import fit.wenchao.simplecodebase.dao.repo.base.pagination.*
import fit.wenchao.simplecodebase.model.JsonResult
import fit.wenchao.simplecodebase.service.ArticleService
import fit.wenchao.simplecodebase.service.SnippetsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero
import javax.validation.constraints.Size


@PageConfig
class QuerySnippet {
    @PageSize
    var pageSize: Long? = null

    @PageNo
    var pageNo: Long? = null

    @Condition(dbFieldName = "lang", con = BaseDao.ConditionEnum.EQ)
    var lang: String? = null

    @Condition(dbFieldName = "title", con = BaseDao.ConditionEnum.LIKE)
    var title: String? = null
}

@PageConfig
class QueryArticle {
    @PageSize
    var pageSize: Long? = null

    @PageNo
    var pageNo: Long? = null

    @Condition(dbFieldName = "title", con = BaseDao.ConditionEnum.LIKE)
    var title: String? = null
}

class UpdateArticleRequest {
    var title: String? = null
    var snippets: MutableList<UpdateSnippetRequest>? = null // Getters and setters
}

class CreateArticleRequest {
    @NotBlank
    @Size(max = 255)
    var title: String? = null

    @NotNull
    @Valid
    var snippets: MutableList<CreateSnippetRequest>? = null // Getters and setters
}

class CreateSnippetRequest {
    @NotBlank
    var content: String? = null

    @NotNull
    @PositiveOrZero
    var order: Int? = null
    var lang: String? = null
    var description: String? = null // Getters and setters
}

class UpdateSnippetRequest {
    @NotNull
    var id: Long? = null
    var content: String? = null

    @NotNull
    @PositiveOrZero
    var order: Int? = null
    var lang: String? = null
    var description: String? = null // Getters and setters
}


class ArticleVO {

    var id: Long? = null

    var title: String? = null

    var createTime: String? = null

    var snippets: MutableList<SnippetVO>? = null // Getters and setters
}


data class SnippetVO(
    var id: Long? = null,
    var content: String? = null,
    var order: Int? = null,
    var lang: String? = null,
    var description: String? = null
)


@Validated
@RestController
@RequestMapping("/API")
class CodeBaseController {

    /// new api

    @Autowired
    lateinit var articleService: ArticleService

    @Autowired
    lateinit var snippetsService: SnippetsService

    // Create a new article
    @PostMapping("/article")
    fun createArticle(@RequestBody createArticleRequest: CreateArticleRequest): Any {
        val articleId: Long = articleService.createArticle(createArticleRequest)
        return articleId
    }

    // Delete an existing article
    @DeleteMapping("/article/{articleId}")
    fun deleteArticle(@PathVariable articleId: Long): Any {
        articleService.deleteArticle(articleId)
        return JsonResult.ok()
    }

    // Update an existing article
    @PutMapping("/article/{articleId}")
    fun updateArticle( @PathVariable articleId: Long, @RequestBody updateArticleRequest: UpdateArticleRequest ): Any {
        articleService.updateArticle(articleId, updateArticleRequest)
        return JsonResult.ok()
    }



    // List articles
    @GetMapping("/articles")
    fun listArticles(queryArticle: QueryArticle): Any {
        val page: Page<ArticlePO> = articleService.listArticles(queryArticle)
        return JsonResult.ok(object {
            var records = page.records
            var total = page.total
        })
    }


    @GetMapping("/article/snippets/{articleId}")
    fun listSnippetsForArticle(@PathVariable articleId: Long): Any {
        val snippets: List<SnippetVO> = snippetsService.listSnippetsForArticle(articleId)
        return snippets
    }

}

