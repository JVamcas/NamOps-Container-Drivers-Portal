package com.pet001kambala.namopscontainers.utils

import android.os.Build
import android.text.format.DateFormat
import androidx.annotation.RequiresApi
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.sql.Timestamp
import java.util.*

class DateUtil {
    companion object {
        const val DATE_FORMAT = "yyyy_MM_dd HH_mm_ss"
        var ICON_PATH_PATTERN = ".*(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}).*"
        val INCUBATION_TIME = 86400000 * 14

        fun today(): Date {
            return Date()
        }

        fun Date._24(): String {
            return SimpleDateFormat(DATE_FORMAT, Locale.US).format(this)
        }

        fun parseDate(date: String?): Date? {
            try {
                return SimpleDateFormat(DATE_FORMAT, Locale.US).parse(date!!)
            } catch (ignore: ParseException) {
            }
            return null
        }

        fun getDate(dateStr: String): String {
            return dateStr.replace(".*(\\d{4}-\\d{2}-\\d{2}).*".toRegex(), "$1")
        }

        fun getTime(dateStr: String): String {
            return dateStr.replace(".*(\\d{2}:\\d{2}:\\d{2}).*".toRegex(), "$1")
        }

        fun fromLong(long: Long): String {
            return DateFormat.format("dd MMMM yyyy", Date(long)).toString()
        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun LocalDateTime._24(): String{
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            return this.format(formatter)
        }
        fun localDateToday(): LocalDateTime{
            val date = Date()
            return Timestamp(date.time).toLocalDateTime()
        }

    }
}