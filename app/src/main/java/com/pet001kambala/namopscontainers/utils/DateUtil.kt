package com.pet001kambala.namopscontainers.utils

import android.text.format.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class DateUtil {
    companion object {
        const val DATE_FORMAT = "yyyy_MM_dd HH_mm_ss"
        const val LOCAL_DATE_FORMAT = ""


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

        fun LocalDateTime._24(): String {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            return this.format(formatter)
        }

        fun localDateToday(): LocalDateTime {
            val date = Date()
            return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        }
    }
}


