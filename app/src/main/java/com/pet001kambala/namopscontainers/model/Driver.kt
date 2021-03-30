package com.pet001kambala.namopscontainers.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity
data class Driver(
    val firstName: String = "",
    val lastName: String = "",
    val passCode: String = "",
    @PrimaryKey(autoGenerate = true)
    override var id: Int?
) : AbstractModel(id = id) {


    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Driver)
            return false
        return other.id == id
    }

    override fun toString(): String {
        return "$firstName $lastName"
    }

    override fun hashCode(): Int {
        var result = firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        return result
    }

    fun isUnknownDriver(): Boolean{
        return firstName.toLowerCase() =="unknown"
    }

}