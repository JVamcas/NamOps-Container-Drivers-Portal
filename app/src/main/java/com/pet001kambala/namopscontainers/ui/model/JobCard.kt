package com.pet001kambala.namopscontainers.ui.model

import androidx.annotation.Keep

/**
 * [JobCard] can have multiple containers, it might be pre-assigned in which case [driverId] will be set
 */
@Keep
data class JobCard(
    val containerNo: String? = null,
    val containerSize: String? = null,
    val isFull: Boolean = false,
    val customerRef: String? = null,
    val driverId: Long? = null
) : AbstractModel()