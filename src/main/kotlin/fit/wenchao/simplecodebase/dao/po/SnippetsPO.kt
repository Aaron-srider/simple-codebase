package fit.wenchao.simplecodebase.dao.po

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable

@TableName("`snippets`")
data class SnippetsPO(
    @TableId(value = "id", type = IdType.AUTO)
    var id: Long?,
    var codeContent: String?,
    var lang: String?,
    var title: String?,
    var createTime: String?,
    var description: String?,
    var articleId: Int?,
    var order: Int?,
) : Serializable {
    constructor() : this(null, null, null, null, null, null, null, null)
}