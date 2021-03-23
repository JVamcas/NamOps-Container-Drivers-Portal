package com.pet001kambala.namopscontainers.utils

import androidx.room.TypeConverter
import com.pet001kambala.namopscontainers.model.*
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.convert
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.toJson
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

class JobCardItemConverter {

    @TypeConverter
    fun toJobCardItemList(json: String?): List<JobCardItem>? {
        return json?.convert()
    }

    @TypeConverter
    fun fromJobCardItemList(list: List<JobCardItem>?): String? {
        return list?.toJson()
    }
}

class DriverConverter {

    @TypeConverter
    fun toDriver(json: String?): Driver? {
        return json?.convert()
    }

    @TypeConverter
    fun fromDriver(driver: Driver?): String? {
        return driver?.toJson()
    }
}

class TripConverter {

    @TypeConverter
    fun toTrip(json: String?): Trip? {
        return json?.convert()
    }

    @TypeConverter
    fun fromTrip(trip: Trip?): String? {
        return trip?.toJson()
    }
}