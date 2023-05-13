package fit.wenchao.simplecodebase.dao.repo.impl
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import fit.wenchao.simplecodebase.dao.mapper.SnippetsTagMapper
import fit.wenchao.simplecodebase.dao.po.SnippetsTagPO
import fit.wenchao.simplecodebase.dao.repo.SnippetsTagDao
import org.springframework.stereotype.Repository
@Repository
class SnippetsTagDaoImpl : ServiceImpl<SnippetsTagMapper,SnippetsTagPO>() , SnippetsTagDao {
}
