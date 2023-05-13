package fit.wenchao.simplecodebase.dao.po
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable
@TableName("`tag`")
data class TagPO (
@TableId(value="id", type=IdType.AUTO)
var id: Long ?,
var name: String ?
)
: Serializable 