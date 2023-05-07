package fit.wenchao.simplecodebase.dao.repo.impl
import fit.wenchao.simplecodebase.dao.mapper.ArticleMapper
import fit.wenchao.simplecodebase.dao.po.ArticlePO
import fit.wenchao.simplecodebase.dao.repo.ArticleDao
import fit.wenchao.simplecodebase.dao.repo.base.BaseDao
import org.springframework.stereotype.Repository
@Repository
class ArticleDaoImpl : BaseDao<ArticleMapper,ArticlePO>() , ArticleDao {
}
