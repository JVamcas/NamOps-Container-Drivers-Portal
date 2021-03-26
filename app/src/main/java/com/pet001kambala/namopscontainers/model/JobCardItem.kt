package com.pet001kambala.namopscontainers.model

import androidx.annotation.Keep
import java.time.LocalDateTime
import java.util.*

/**
 * [JobCardItem] can have multiple containers, it might be pre-assigned in which case [driver] will be set
 */
@Keep
data class JobCardItem(
    override var id: Int?,
    val jobCardNo: String,
    val containerNo: String? = null,
    val containerSize: String? = null,
    val pickUpLocationName: String? = null,
    val isFull: Boolean = false,
    val customerRef: String? = null,
    val driver: Driver? = null, //assigned if container is full
    var jobCardCompleted: Boolean = false, // whether or not the job card item is completed so as not to include it again.
    var wasPickedUp: Boolean = false,
    val scanContainer: Boolean = false,
    val useBison: Boolean  = false,
    val useWeighBridge: Boolean = false,
    val designatePickUpDate: LocalDateTime? = null,
    val weighBridgeName: String? = null
) : AbstractModel()