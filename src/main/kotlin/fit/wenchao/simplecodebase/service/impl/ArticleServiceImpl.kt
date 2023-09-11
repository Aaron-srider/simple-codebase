package fit.wenchao.simplecodebase.service.impl

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
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
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.streams.toList

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

    override fun listArticles(pageSize: Int, pageNo: Int, keyWord: String?): Page<ArticlePO> {
        keyWord?.let {
            // search title
            var titleResult = articleDao.list(QueryWrapper<ArticlePO>().like("title", keyWord))
            var codeContentResult = snippetsDao.list(QueryWrapper<SnippetsPO>().like("code_content", keyWord))
            var descResult = snippetsDao.list(QueryWrapper<SnippetsPO>().like("description", keyWord))

            var contentResultStream = Stream.concat(descResult.stream(), codeContentResult.stream())
                .filter { it.articleId != null }
                .collect(
                    Collectors.toMap(
                        { key -> key.id.toString() + key.articleId.toString() },
                        { value -> value },
                        { existing, replacement -> existing })
                )
                .values
                .stream()
                .map { it.articleId!! }
                .map { articleDao.getById(it) }
                .filter { it != null }

            val restults = Stream.concat(titleResult.stream(), contentResultStream)
                .collect(Collectors.toMap({ key -> key.id }, { value -> value }, { existing, replacement -> existing }))
                .values
                .stream().toList()

            var page = Page<ArticlePO>()
            page.pageNo = pageNo.toLong()
            page.pageSize = pageSize.toLong()
            page.total = restults.size.toLong()

            page.records = restults.stream()
                .sorted { o1, o2 -> o1.updateTime?.compareTo(o2.updateTime ?: "") ?: 0 }
                .skip((pageNo - 1) * pageSize.toLong())
                .limit(pageSize.toLong())
                .toList()
            return page
        } ?: run {
            return articleDao.pageData(QueryArticle().apply {
                this.pageSize = pageSize.toLong()
                this.pageNo = pageNo.toLong()
            }, null)
        }
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

    override fun getArticle(articleId: Long): ArticlePO {
        return articleDao.getById(articleId) ?: throw BackendException(null, RespCode.ARTICLE_NOT_FOUND)
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
