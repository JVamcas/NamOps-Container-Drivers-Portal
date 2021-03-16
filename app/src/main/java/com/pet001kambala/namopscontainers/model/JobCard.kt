package com.pet001kambala.namopscontainers.model

data class JobCard(
    val jobCardNo: String,
    val totalContainers: Int,
    var pendingContainers: Int,
    var pickUpLocation: String,
    var jobCardItemList: List<JobCardItem>

): AbstractModel() {

    override fun toString(): String {
        return "Pick up $pendingContainers from $pickUpLocation"
    }
}