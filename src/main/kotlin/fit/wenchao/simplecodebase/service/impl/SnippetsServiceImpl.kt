package fit.wenchao.simplecodebase.service.impl

import fit.wenchao.simplecodebase.dao.po.*
import fit.wenchao.simplecodebase.dao.repo.SnippetsDao
import fit.wenchao.simplecodebase.exception.BackendException
import fit.wenchao.simplecodebase.exception.RespCode
import fit.wenchao.simplecodebase.rest.ExchangeOrderRequest
import fit.wenchao.simplecodebase.rest.ExchangeOrderResponse
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
        val snippets = snippetsDao.listByMap("article_id", articleId)
        return snippets.map { snippet -> snippet.toVO() }
    }

    @Transactional
    override fun createSnippet(articleId: Long, createSnippetRequest: CreateSnippetRequest): InsertSnippetResponse {
        // update order of the rest snippets after the new snippet of article
        var snippets = snippetsDao.listByMap("article_id", articleId)

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

        val snippetIdAndOrderMap = getOrderMapOfSnippetsByArticleId(articleId = articleId)

        return InsertSnippetResponse(
            newSnippetHandle = snippetPO.id,
            orderMap = snippetIdAndOrderMap
        )
    }

    @Transactional
    override fun deleteSnippet(snippetId: Long): DeleteSnippetResponse {

        // get the article id of the snippet
        val snippet = snippetsDao.getById(snippetId)
        snippet ?: throw BackendException(null, RespCode.SNIPPET_NOT_FOUND)

        val articleId = snippet.articleId

        // remove snippet by id simply
        snippetsDao.removeById(snippetId)

        val snippetIdAndOrderMap = getOrderMapOfSnippetsByArticleId(articleId!!)

        return DeleteSnippetResponse(
            deletedSnippetHandle = snippetId,
            orderMap = snippetIdAndOrderMap
        )
    }

    @Transactional
    override fun exchangeOrder(exchangeOrderRequest: ExchangeOrderRequest): ExchangeOrderResponse {
        var aid = exchangeOrderRequest.snippetAId
        var bid = exchangeOrderRequest.snippetBId

        var aSnippet: SnippetsPO? = null
        var bSnippet: SnippetsPO? = null

        // check existence of two snippet, if aid == bid, check once
        if (aid == bid) {
            aSnippet = snippetsDao.getById(aid!!)
            aSnippet ?: throw BackendException(null, RespCode.SNIPPET_NOT_FOUND)

        } else {
            aSnippet = snippetsDao.getById(aid!!)
            aSnippet ?: throw BackendException(null, RespCode.SNIPPET_NOT_FOUND)

            bSnippet = snippetsDao.getById(bid!!)
            bSnippet ?: throw BackendException(null, RespCode.SNIPPET_NOT_FOUND)

            // check if the two snippets are in the same article

            if (aSnippet.articleId != bSnippet.articleId) {
                throw BackendException(null, RespCode.SNIPPET_NOT_IN_SAME_ARTICLE)
            }

            // exchange order
            val aOrder = aSnippet.order
            val bOrder = bSnippet.order

            aSnippet.order = bOrder
            bSnippet.order = aOrder

            snippetsDao.updateById(aSnippet)
            snippetsDao.updateById(bSnippet)
        }

        val exchangeOrderResponse = ExchangeOrderResponse()
        exchangeOrderResponse.orderMap = getOrderMapOfSnippetsByArticleId(aSnippet.articleId!!)
        return exchangeOrderResponse
    }

    private fun getOrderMapOfSnippetsByArticleId(articleId: Long): Map<Long, Int> {
        // find out a map containing the snippetId and the order
        val snippets = snippetsDao.listByMap("article_id", articleId)

        val snippetIdAndOrderMap = snippets.map { it.id!! to it.order!! }.toMap()
        return snippetIdAndOrderMap
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


