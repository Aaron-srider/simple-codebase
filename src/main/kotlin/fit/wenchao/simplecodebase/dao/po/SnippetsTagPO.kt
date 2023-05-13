package fit.wenchao.simplecodebase.dao.po
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable
@TableName("`snippets_tag`")
data class SnippetsTagPO (
@TableId(value="id", type=IdType.AUTO)
var id: Long ?,
var snippetsId: Long ?,
var tagId: Long ?
)
: Serializable 