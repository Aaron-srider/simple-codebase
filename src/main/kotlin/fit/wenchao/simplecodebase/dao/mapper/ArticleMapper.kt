package fit.wenchao.simplecodebase.dao.mapper
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import fit.wenchao.simplecodebase.dao.po.ArticlePO
import org.apache.ibatis.annotations.Mapper
@Mapper
interface ArticleMapper : BaseMapper<ArticlePO> {
}
