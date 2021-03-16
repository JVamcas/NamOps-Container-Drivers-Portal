package com.pet001kambala.namopscontainers.model

import androidx.annotation.Keep

@Keep
data class Driver(
    val firstName: String = "",
    val lastName: String = "",
    val passCode: String = ""
) : AbstractModel() {


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
}