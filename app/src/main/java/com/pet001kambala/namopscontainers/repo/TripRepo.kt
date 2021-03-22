package com.pet001kambala.namopscontainers.repo

import com.google.gson.JsonParser
import com.pet001kambala.namopscontainers.model.AbstractModel
import com.pet001kambala.namopscontainers.model.Driver
import com.pet001kambala.namopscontainers.model.Trip
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.toJson
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.commons.csv.CSVFormat
import java.io.StringReader
import java.lang.Exception
import kotlin.math.round

class TripRepo {

    var baseUrl: String = "http://160.242.10.200:8081/namops_driver_portal"
    private val client = OkHttpClient.Builder().build()

    suspend fun createNewTrip(passCode: String, trip: Trip): Results {

        val requestBody = FormBody.Builder()
            .add("passcode", passCode)
            .add("trip", trip.toJson())
            .build()

        val request = Request.Builder()
            .url("$baseUrl/trip")
            .post(requestBody)
            .build()

        return try {
            withContext(Dispatchers.IO) {
                val results =
                    client.newCall(request).execute()//wait for the results from the SERVER
                val data = results.body?.string()
                val jsonTree = JsonParser.parseString(data).asJsonObject
                val writeResp = jsonTree.get("data").toString().replace("\"","")

                if (writeResp == "Success")
                    Results.Success(data = arrayListOf(), code = Results.Success.CODE.WRITE_SUCCESS)
                else Results.Error(AbstractModel.ServerException())
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Results.Error(e)
        }
    }


    /***
     * Find the odometer reading for this vehicle from webfleet
     * @param vehicleNo for the vehicle in question
     * @return odometer reading else null
     */
    @InternalCoroutinesApi
    suspend fun findVehicleOdometer(vehicleNo: String): String {
        val url = "https://csv.telematics.tomtom.com/extern?" +
                "account=namops&username=Rauna&password=3Mili2,87&" +
                "apikey=0e7ddb3b-65b0-4991-82a9-1c5c6f312317&lang=en&action=showObjectReportExtern"
        val request = Request.Builder()
            .url(url)
            .build()

        return try {
            withContext(Dispatchers.IO) {
                val results = client.newCall(request).execute()//wait for the results from webfleet
                val data = results.body?.string()
                val csvParser = CSVFormat.newFormat(';').withQuote('"').parse(StringReader(data))
                val vehicleRecord = csvParser.records.filter { it[0] == vehicleNo }
                (round(vehicleRecord.first()[30].toDouble() / 10.0)).toString()// vehicle odometer reading
            }
        } catch (e: Exception) {
            "0.0"
        }
    }
}