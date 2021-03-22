package com.pet001kambala.namopscontainers.utils

import androidx.room.TypeConverter
import com.pet001kambala.namopscontainers.model.TripStatus
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class TripStatusConverter {

    @TypeConverter
    fun toStatus(str: String): TripStatus {
        return TripStatus.valueOf(str)
    }

    @TypeConverter
    fun toStr(tripStatus: TripStatus): String {
        return tripStatus.name
    }
}

class LocalDateConverter {

    @TypeConverter
    fun toDate(timestamp: Long? = null): LocalDateTime? {

        return if (timestamp != null)
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                ZoneId.systemDefault()
            )
        else null
    }

    @TypeConverter
    fun toLong(date: LocalDateTime?): Long {
        return ZonedDateTime.of(date, ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}