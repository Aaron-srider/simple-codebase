package fit.wenchao.simplecodebase.service.impl

import fit.wenchao.simplecodebase.dao.po.*
import fit.wenchao.simplecodebase.dao.repo.ArticleDao
import fit.wenchao.simplecodebase.dao.repo.SnippetsDao
import fit.wenchao.simplecodebase.dao.repo.base.pagination.Page
import fit.wenchao.simplecodebase.exception.BackendException
import fit.wenchao.simplecodebase.exception.RespCode
import fit.wenchao.simplecodebase.service.ArticleService
import fit.wenchao.simplecodebase.utils.DateTimeUtils
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ArticleServiceImpl : ArticleService {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var articleDao: ArticleDao

    @Autowired
    lateinit var snippetsDao: SnippetsDao

    @Transactional
    override fun createArticle(createArticleRequest: CreateArticleRequest): Long {

        // save article first
        var newArtifactHandle = saveArtifact(createArticleRequest)
        logger.info { "create article success, article id: $newArtifactHandle" }

        // save snippets
        saveSnippets(newArtifactHandle, createArticleRequest)

        // return the new artifact handle
        return newArtifactHandle
    }

    /**
     * save snippets
     */
    private fun saveSnippets(newArtifactHandle: Long, createArticleRequest: CreateArticleRequest) {
        // starts save snippets
        val snippets: MutableList<CreateSnippetRequest>? = createArticleRequest.snippets
        var time = DateTimeUtils.nowString()
        snippets?.forEach {
            // convert to snippet po
            val snippetPO = SnippetsPO()
            snippetPO.description = it.description
            snippetPO.codeContent = it.content
            snippetPO.lang = it.lang
            snippetPO.order = it.order
            snippetPO.articleId = newArtifactHandle
            snippetPO.createTime = time
            snippetPO.updateTime = time


            // save snippet
            snippetsDao.save(snippetPO)
            logger.info { "save snippet success, snippet id: ${snippetPO.id}" }
        }
    }

    override fun listArticles(queryArticle: QueryArticle): Page<ArticlePO> {
        return articleDao.pageData(queryArticle, null)
    }

    override fun updateArticle(articleId: Long, updateArticleRequest: UpdateArticleRequest) {
        // find the existing article by id
        val existingArticle: ArticlePO? = articleDao.getById(articleId)
        existingArticle ?: throw BackendException(null, RespCode.ARTICLE_NOT_FOUND)

        // update the article fields
        existingArticle.title = updateArticleRequest.title ?: existingArticle.title

        existingArticle.updateTime = DateTimeUtils.nowString()

        // update the snippets, if any
        val snippets = updateArticleRequest.snippets
        snippets?.forEach { updateSnippet ->
            val existingSnippet = snippetsDao.getById(updateSnippet.id)
            existingSnippet?.let {
                it.order = updateSnippet.order ?: existingSnippet.order
                it.updateTime = DateTimeUtils.nowString()
                it.codeContent = updateSnippet.content ?: existingSnippet.codeContent
                it.lang = updateSnippet.lang ?: existingSnippet.lang
                it.description = updateSnippet.description ?: existingSnippet.description
                snippetsDao.updateById(it)
                logger.info { "updated snippet with id: ${existingSnippet.id}" }
            }
        }

        // update the article
        articleDao.updateById(existingArticle)
        logger.info { "updated article with id: $articleId" }
    }

    override fun deleteArticle(articleId: Long) {

        val articleOptional = articleDao.getById(articleId)
        articleOptional?.let {
            // Delete the associated snippets first
            val snippets = snippetsDao.listByMap("article_id", it.id)
            snippets.forEach { snippetsDao.removeById(it.id) }

            // Delete the article
            articleDao.removeById(it.id)

            logger.info { "Deleted article with ID: $articleId" }
        } ?: run {
            throw BackendException(null, RespCode.ARTICLE_NOT_FOUND)
        }

    }

    /**
     * save article
     */
    private fun saveArtifact(createArticleRequest: CreateArticleRequest): Long {
        val title = createArticleRequest.title
        val createTime = DateTimeUtils.nowString()

        // save article first
        var article = ArticlePO()
        article.title = title ?: "Untitled"
        article.createTime = createTime
        article.updateTime = createTime
        articleDao.save(article)

        // get the new artifact handle
        var newArtifactHandle = article.id as Long
        return newArtifactHandle
    }
}
