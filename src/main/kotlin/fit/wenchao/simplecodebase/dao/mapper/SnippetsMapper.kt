package fit.wenchao.simplecodebase.dao.mapper
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import fit.wenchao.simplecodebase.dao.po.SnippetsPO
import org.apache.ibatis.annotations.Mapper
@Mapper
interface SnippetsMapper : BaseMapper<SnippetsPO> {
}
