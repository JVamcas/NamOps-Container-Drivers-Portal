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
        return "Pick up $pendingContainers containers from $pickUpLocationName"
    }
}