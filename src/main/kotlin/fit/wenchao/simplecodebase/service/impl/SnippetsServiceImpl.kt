package fit.wenchao.simplecodebase.service.impl

import fit.wenchao.simplecodebase.dao.po.CreateSnippetRequest
import fit.wenchao.simplecodebase.dao.po.InsertSnippetResponse
import fit.wenchao.simplecodebase.dao.po.SnippetVO
import fit.wenchao.simplecodebase.dao.po.SnippetsPO
import fit.wenchao.simplecodebase.dao.repo.SnippetsDao
import fit.wenchao.simplecodebase.service.SnippetsService
import fit.wenchao.simplecodebase.utils.DateTimeUtils
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SnippetsServiceImpl : SnippetsService {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var snippetsDao: SnippetsDao

    override fun listSnippetsForArticle(articleId: Long): List<SnippetVO> {
        val snippets = snippetsDao.listByMap("article_id",articleId)
        return snippets.map { snippet -> snippet.toVO() }
    }

    @Transactional
    override fun createSnippet(articleId: Long, createSnippetRequest: CreateSnippetRequest):InsertSnippetResponse {
        // update order of the rest snippets after the new snippet of article
        var snippets = snippetsDao.listByMap("article_id",articleId)

        // update order of the rest snippets after the new snippet of article
        snippets.forEach {
            if (it.order!! >= createSnippetRequest.order!!) {
                it.order = it.order!! + 1
                snippetsDao.updateById(it)
            }
        }


        var time = DateTimeUtils.nowString()

        // convert to snippet po
        val snippetPO = SnippetsPO()

        snippetPO.description = createSnippetRequest.description
        snippetPO.codeContent = createSnippetRequest.content
        snippetPO.lang = createSnippetRequest.lang
        snippetPO.order = createSnippetRequest.order
        snippetPO.articleId = articleId
        snippetPO.createTime = time
        snippetPO.updateTime = time

        // save snippet
        snippetsDao.save(snippetPO)
        logger.info { "save snippet success, snippet id: ${snippetPO.id}" }

        // find out a map containing the snippetId and the order
        snippets = snippetsDao.listByMap("article_id",articleId)

        val snippetIdAndOrderMap = snippets.map { it.id!! to it.order!! }.toMap()

        return InsertSnippetResponse(
            newSnippetHandle = snippetPO.id,
            orderMap = snippetIdAndOrderMap
        )
    }

    // Extension function to convert a Snippet entity to a SnippetVO
     fun SnippetsPO.toVO(): SnippetVO {
        return SnippetVO(
            id = this.id,
            content = this.codeContent,
            order = this.order,
            lang = this.lang,
            description = this.description
        )
    }

}
