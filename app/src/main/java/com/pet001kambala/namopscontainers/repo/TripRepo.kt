package com.pet001kambala.namopscontainers.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.commons.csv.CSVFormat
import java.io.StringReader
import kotlin.math.round

class TripRepo {













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
        val client = OkHttpClient.Builder().build()
        val request = Request.Builder()
            .url(url)
            .build()

        return try {
            withContext(Dispatchers.IO) {
                val results = client.newCall(request).execute()//wait for the results from webfleet
                val data = results.body?.string()
                val csvParser = CSVFormat.newFormat(';').withQuote('"').parse(StringReader(data))
                val vehicleRecord = csvParser.records.filter { it[0] == vehicleNo }
                (round(vehicleRecord.first()[30].toDouble()/10.0)).toString()// vehicle odometer reading
            }
        } catch (e: java.lang.Exception) {
            "0.0"
        }
    }
}