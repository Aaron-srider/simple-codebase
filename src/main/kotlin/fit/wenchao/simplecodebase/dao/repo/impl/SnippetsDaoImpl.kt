package fit.wenchao.simplecodebase.dao.repo.impl
import fit.wenchao.simplecodebase.dao.mapper.SnippetsMapper
import fit.wenchao.simplecodebase.dao.po.SnippetsPO
import fit.wenchao.simplecodebase.dao.repo.SnippetsDao
import fit.wenchao.simplecodebase.dao.repo.base.BaseDao
import org.springframework.stereotype.Repository
@Repository
class SnippetsDaoImpl : BaseDao<SnippetsMapper,SnippetsPO>() , SnippetsDao {
}
