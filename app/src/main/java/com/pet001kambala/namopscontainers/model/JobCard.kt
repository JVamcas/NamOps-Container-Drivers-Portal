package com.pet001kambala.namopscontainers.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.pet001kambala.namopscontainers.utils.JobCardItemConverter

@Entity
data class JobCard(
    @PrimaryKey(autoGenerate = true)
    override var id: Int? = 0,
    val jobCardNo: String,
    val totalContainers: Int,
    var pendingContainers: Int,
    var pickUpLocationName: String

): AbstractModel() {

    @TypeConverters(JobCardItemConverter::class)
    var jobCardItemList: List<JobCardItem>? = null

    override fun toString(): String {
        return "Pick up $totalContainers containers from $pickUpLocationName"
    }

    fun filterPickedUpContainers(trip: Trip): List<JobCardItem>{
        val containers = arrayListOf(trip.container1,trip.container2,trip.container3).filterNotNull()

        return jobCardItemList?.filter { it.containerNo in containers }!!
    }
}