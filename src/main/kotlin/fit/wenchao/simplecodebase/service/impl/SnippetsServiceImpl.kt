package fit.wenchao.simplecodebase.service.impl

import fit.wenchao.simplecodebase.dao.po.SnippetVO
import fit.wenchao.simplecodebase.dao.po.SnippetsPO
import fit.wenchao.simplecodebase.dao.repo.SnippetsDao
import fit.wenchao.simplecodebase.service.SnippetsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SnippetsServiceImpl : SnippetsService {

    @Autowired
    lateinit var snippetsDao: SnippetsDao

    override fun listSnippetsForArticle(articleId: Long): List<SnippetVO> {
        val snippets = snippetsDao.listByMap("article_id",articleId)
        return snippets.map { snippet -> snippet.toVO() }
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
