package fit.wenchao.simplecodebase.service

import fit.wenchao.simplecodebase.dao.po.CreateSnippet
import fit.wenchao.simplecodebase.dao.po.QuerySnippet
import fit.wenchao.simplecodebase.dao.po.SnippetsPO
import fit.wenchao.simplecodebase.dao.po.UpdateSnippet
import fit.wenchao.simplecodebase.dao.repo.base.pagination.Page

interface SnippetsService {
    fun findById(id: Long): SnippetsPO?
    fun create(snippet: CreateSnippet)

    fun findAll(querySnippet: QuerySnippet): Page<SnippetsPO>
    fun removeSnippetById(id: Long)
    fun save(id: Long, snippet: UpdateSnippet)
}
