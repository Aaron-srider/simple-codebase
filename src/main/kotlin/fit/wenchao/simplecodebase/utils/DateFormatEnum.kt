package fit.wenchao.simplecodebase.utils

import cn.hutool.core.date.DateTime
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    /**
     * Constants represent common date format string
     */
    enum class DateFormatEnum(val formatString: String) {
        UGLY_DEFAULT_ONE("EEE MMM dd HH:mm:ss zzz yyyy"),

        DATE_TIME("yyyy-MM-dd hh:mm:ss"),

        DATE_ONLY("yyyy-MM-dd")
    }

    fun nowString(): String {
        val now = DateTime.now()
        val nowString = now.toString("yyyy-MM-dd HH:mm:ss")
        return nowString
    }

    fun format(date: Date): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateString = formatter.format(date)
        return dateString
    }
}