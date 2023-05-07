package fit.wenchao.simplecodebase.service

import fit.wenchao.simplecodebase.dao.po.SnippetVO


interface SnippetsService {

    fun listSnippetsForArticle(articleId: Long): List<SnippetVO>

}
