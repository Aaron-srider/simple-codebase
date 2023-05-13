package fit.wenchao.simplecodebase.dao.po

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable

@TableName("`article`")
data class ArticlePO(
    @TableId(value = "id", type = IdType.AUTO)
    var id: Long?,
    var title: String?,
    var createTime: String?,
    var updateTime: String?
) : Serializable {
    constructor() : this(null,null, null, null)
}