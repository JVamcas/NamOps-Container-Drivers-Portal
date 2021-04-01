package com.pet001kambala.namopscontainers.repo

import com.google.gson.JsonParser
import com.pet001kambala.namopscontainers.model.AbstractModel
import com.pet001kambala.namopscontainers.model.Driver
import com.pet001kambala.namopscontainers.model.JobCard
import com.pet001kambala.namopscontainers.model.JobCardItem
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.convert
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.toJson
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class JobCardRepo {


    suspend fun loadPreAssignedJobCards(driver: Driver): Results {

        val results = loadAllJobCardItemsOnIncmpleteJobCards(driver)

        return if (results is Results.Success<*>) {
            val jobCardItems = results.data as ArrayList<JobCardItem>
            val filteredJobCardItems = jobCardItems.filter { it.driver?.id ?: -1 == driver.id }
            val jobCardList = if (filteredJobCardItems.isNullOrEmpty()) {
                filteredJobCardItems.groupBy { it.jobCardNo }
                    .map { item ->
                        JobCard(
                            jobCardNo = item.key,
                            totalContainers = item.value.size,
                            pickUpLocationName = item.value[0].pickUpLocationName ?: "Unknown",
                            pendingContainers = item.value.count { !it.wasPickedUp }).also {
                            it.jobCardItemList = item.value
                        }
                    }.filter { it.pendingContainers > 0 }
            } else arrayListOf()

            Results.Success(data = ArrayList(jobCardList), code = Results.Success.CODE.LOAD_SUCCESS)

        } else results
    }

    suspend fun loadAllJobCards(driver: Driver, driverId: Int? = 0): Results {
        val results = loadAllJobCardItemsOnIncmpleteJobCards(driver)
        return if (results is Results.Success<*>) {
            val jobCardItems = results.data as ArrayList<JobCardItem>
            val filteredJobCardItems = jobCardItems.filter { it.driver?.id ?: -1 == driverId }
            val jobCardList = filteredJobCardItems.groupBy { it.jobCardNo }
            val other = jobCardList.map { entry ->
                JobCard(
                    jobCardNo = entry.key,
                    totalContainers = entry.value.size,
                    pickUpLocationName = entry.value[0].pickUpLocationName ?: "Unknown",
                    pendingContainers = entry.value.count { !it.wasPickedUp })
                    .also { it.jobCardItemList = entry.value }
            }.filter { it.pendingContainers > 0 }

            Results.Success(data = ArrayList(other), code = Results.Success.CODE.LOAD_SUCCESS)

        } else results
    }

    private suspend fun loadAllJobCardItemsOnIncmpleteJobCards(driver: Driver): Results {
        val url = "http://160.242.10.200:8081/namops_driver_portal/all_job_cards?surname=${driver.lastName}&passcode=${driver.passCode}"
        val client = OkHttpClient.Builder().build()
        val request = Request.Builder()
            .url(url)
            .build()

        return try {
            withContext(Dispatchers.IO) {
                val results = client.newCall(request).execute()//wait for the results from api
                val data = results.body?.string()

                val jsonTree = JsonParser.parseString(data).asJsonObject

                when (jsonTree.get("Status").toString().replace("\"", "")) {
                    "Success" -> {
                        val jsonData = jsonTree.get("data").toString()
                        val jobCardItems = if (jsonData.isEmpty())
                            arrayListOf() else jsonData.convert<ArrayList<JobCardItem>>()
                        Results.Success(
                            data = jobCardItems,
                            code = Results.Success.CODE.LOAD_SUCCESS
                        )
                    }
                    "Invalid Auth" -> Results.Error(AbstractModel.InvalidAuthCredException())

                    else -> Results.Error(AbstractModel.ServerException())
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Results.Error(e)
        }
    }
}