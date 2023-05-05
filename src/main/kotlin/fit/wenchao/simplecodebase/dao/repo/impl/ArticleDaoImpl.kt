package fit.wenchao.simplecodebase.dao.repo.impl
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import fit.wenchao.simplecodebase.dao.mapper.ArticleMapper
import fit.wenchao.simplecodebase.dao.po.ArticlePO
import fit.wenchao.simplecodebase.dao.repo.ArticleDao
import org.springframework.stereotype.Repository
@Repository
class ArticleDaoImpl : ServiceImpl<ArticleMapper,ArticlePO>() , ArticleDao {
}
