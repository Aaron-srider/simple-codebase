package fit.wenchao.simplecodebase.service

import fit.wenchao.simplecodebase.dao.po.CreateSnippet
import fit.wenchao.simplecodebase.dao.po.SnippetsPO
import fit.wenchao.simplecodebase.dao.po.UpdateSnippet

interface SnippetsService {
    fun findById(id: Long): SnippetsPO?
     fun save(snippet: CreateSnippet)

     fun findAll(): MutableList<SnippetsPO>
    fun removeSnippetById(id: Long)
    fun update(id: Long, snippet: UpdateSnippet)
}
