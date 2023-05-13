package fit.wenchao.simplecodebase.dao.repo.impl
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import fit.wenchao.simplecodebase.dao.mapper.TagMapper
import fit.wenchao.simplecodebase.dao.po.TagPO
import fit.wenchao.simplecodebase.dao.repo.TagDao
import org.springframework.stereotype.Repository
@Repository
class TagDaoImpl : ServiceImpl<TagMapper,TagPO>() , TagDao {
}
