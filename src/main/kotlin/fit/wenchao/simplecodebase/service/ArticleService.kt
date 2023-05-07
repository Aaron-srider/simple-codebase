package fit.wenchao.simplecodebase.service

import fit.wenchao.simplecodebase.dao.po.ArticlePO
import fit.wenchao.simplecodebase.dao.po.CreateArticleRequest
import fit.wenchao.simplecodebase.dao.po.QueryArticle
import fit.wenchao.simplecodebase.dao.po.UpdateArticleRequest
import fit.wenchao.simplecodebase.dao.repo.base.pagination.Page

interface ArticleService {
    fun createArticle(createArticleRequest: CreateArticleRequest): Long
    fun listArticles(queryArticle: QueryArticle):  Page<ArticlePO>
    fun deleteArticle(articleId: Long)
    fun updateArticle(articleId: Long, updateArticleRequest: UpdateArticleRequest)
}
