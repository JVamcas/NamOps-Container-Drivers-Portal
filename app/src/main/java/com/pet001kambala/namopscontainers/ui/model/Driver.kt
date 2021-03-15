package com.pet001kambala.namopscontainers.ui.model

import androidx.annotation.Keep

@Keep
enum class AuthType {
    EMAIL, PHONE
}

@Keep
data class Driver(
    val firstName: String? = null,
    val lastName: String? = null
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
        var result = firstName?.hashCode() ?: 0
        result = 31 * result + (lastName?.hashCode() ?: 0)
        return result
    }
}