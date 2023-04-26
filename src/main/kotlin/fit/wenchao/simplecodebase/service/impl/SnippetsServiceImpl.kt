package fit.wenchao.simplecodebase.service.impl
import fit.wenchao.simplecodebase.consts.RespCode
import fit.wenchao.simplecodebase.dao.po.CreateSnippet
import fit.wenchao.simplecodebase.dao.po.SnippetsPO
import fit.wenchao.simplecodebase.dao.po.UpdateSnippet
import fit.wenchao.simplecodebase.dao.repo.SnippetsDao
import fit.wenchao.simplecodebase.exception.BackendException
import fit.wenchao.simplecodebase.service.SnippetsService
import fit.wenchao.simplecodebase.utils.DateTimeUtils
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SnippetsServiceImpl : SnippetsService {

    @Autowired
    lateinit var snippetsDao: SnippetsDao

    override fun findById(id: Long): SnippetsPO? {
        return snippetsDao.getById(id)
    }

    override fun create(snippet: CreateSnippet) {
        var saveSnippet = SnippetsPO()
        BeanUtils.copyProperties(snippet, saveSnippet)
        saveSnippet.createTime = DateTimeUtils.nowString()
        snippetsDao.save(saveSnippet)
    }

    override fun save(id: Long, snippet: UpdateSnippet) {
        val existingSnippet = findById(id)
        var snippetsPO = SnippetsPO()
        snippetsPO.codeContent = snippet.codeContent
        snippetsPO.lang = snippet.lang
        snippetsPO.title = snippet.title
        snippetsPO.description = snippet.description
        snippetsPO.id = id
        existingSnippet?.let {
            snippetsDao.updateById(snippetsPO)
        } ?: run {
            throw BackendException(null, RespCode.NO_SNIPPET)
        }
    }


    override fun findAll(): MutableList<SnippetsPO> {
        val list = snippetsDao.list()
        return list
    }

    override fun removeSnippetById(id: Long) {
        snippetsDao.removeById(id)
    }
}
