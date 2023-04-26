package fit.wenchao.simplecodebase.dao.repo.impl
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import fit.wenchao.simplecodebase.dao.mapper.SnippetsMapper
import fit.wenchao.simplecodebase.dao.po.SnippetsPO
import fit.wenchao.simplecodebase.dao.repo.SnippetsDao
import org.springframework.stereotype.Repository
@Repository
class SnippetsDaoImpl : ServiceImpl<SnippetsMapper,SnippetsPO>() , SnippetsDao {
}
