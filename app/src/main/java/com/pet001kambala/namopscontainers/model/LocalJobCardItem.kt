package com.pet001kambala.namopscontainers.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.pet001kambala.namopscontainers.utils.TripConverter


@Entity
class LocalJobCardItem: AbstractModel() {
    @PrimaryKey(autoGenerate = true)
    override var id: Int? = null

    @TypeConverters(TripConverter::class)
    var trip: Trip? = null

}