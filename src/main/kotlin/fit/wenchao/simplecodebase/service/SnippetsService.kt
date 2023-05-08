package fit.wenchao.simplecodebase.service

import fit.wenchao.simplecodebase.dao.po.CreateSnippetRequest
import fit.wenchao.simplecodebase.dao.po.DeleteSnippetResponse
import fit.wenchao.simplecodebase.dao.po.InsertSnippetResponse
import fit.wenchao.simplecodebase.dao.po.SnippetVO
import fit.wenchao.simplecodebase.rest.ExchangeOrderRequest
import fit.wenchao.simplecodebase.rest.ExchangeOrderResponse


interface SnippetsService {

    fun listSnippetsForArticle(articleId: Long): List<SnippetVO>
    fun createSnippet(articleId: Long, createSnippetRequest: CreateSnippetRequest): InsertSnippetResponse
    fun deleteSnippet(snippetId: Long): DeleteSnippetResponse
    fun exchangeOrder(exchangeOrderRequest: ExchangeOrderRequest): ExchangeOrderResponse

}
