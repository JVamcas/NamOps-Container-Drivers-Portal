package com.pet001kambala.namopscontainers.model

import androidx.annotation.Keep

/**
 * [JobCardItem] can have multiple containers, it might be pre-assigned in which case [driverId] will be set
 */
@Keep
data class JobCardItem(
    val jobCardNo: Int,
    val containerNo: String? = null,
    val containerSize: String? = null,
    val isFull: Boolean = false,
    val customerRef: String? = null,
    val driverId: Int? = null, //assigned if container is full
    val isCompleted: Boolean = false // whether or not the job card item is completed so as not to include it again.
) : AbstractModel()