package com.pet001kambala.namopscontainers.repo

import android.app.Application
import android.content.Context
import android.text.TextUtils
import androidx.room.*
import com.google.gson.JsonParser
import com.pet001kambala.namopscontainers.model.*
import com.pet001kambala.namopscontainers.utils.Const.Companion.baseUrl
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.convert
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.toJson
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.commons.csv.CSVFormat
import java.io.StringReader
import kotlin.math.round

class TripRepo(val app: Application? = null) {

    private val client = OkHttpClient.Builder().build()
    val tripDao by lazy { TripDatabase.getDatabase(app!!).tripDao() }


    /**
     * 1. First write to the backend persist returned trip to the local db
     * 2. If backend fail, write to local and set [LocalTrip.awaitingNetwork] true
     */
    suspend fun createNewTrip(driver: Driver, localTrip: LocalTrip): Results {
        return try {

            //1. first write to the repository
            val requestBody = FormBody.Builder()
                .add("passcode", driver.passCode)
                .add("surname", driver.lastName)
                .add("trip", localTrip.trip.toJson())
                .build()

            val request = Request.Builder()
                .url("$baseUrl/trip_update")
                .post(requestBody)
                .build()

            withContext(Dispatchers.IO) {

                val results =
                    client.newCall(request).execute()//wait for the results from the SERVER
                val data = results.body?.string()
                val jsonTree = JsonParser.parseString(data).asJsonObject
                val writeResp = jsonTree.get("Status").toString().replace("\"", "")

                if (writeResp == "Success") {
                    val jsonData = jsonTree.get("data").toString()
                    val trip = jsonData.convert<Trip>()
                    localTrip.trip = trip
                    localTrip.awaitingNetwork = false

                    tripDao.clearTripTable()
                    tripDao.insertTrip(localTrip)

                    Results.Success(
                        data = arrayListOf(localTrip),
                        code = Results.Success.CODE.WRITE_SUCCESS
                    )
                } else Results.Error(AbstractModel.ServerException())
            }
        } catch (e: Exception) {
            try {
                localTrip.awaitingNetwork = true
                tripDao.insertTrip(localTrip)
            } catch (e: Exception) {
                //should not come here
                e.printStackTrace()
                Results.Error(e)
            }
            e.printStackTrace()
            Results.Success(
                data = arrayListOf(localTrip),
                code = Results.Success.CODE.AWAITING_NETWORK
            )
        }
    }

    private suspend fun loadDriverRecentTrip(driver: Driver): Results {
        val url =
            "$baseUrl/recent_incomplete_trip?surname=${driver.lastName}&passcode=${driver.passCode}"
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
                        val trip = if (jsonData.isEmpty())
                            null else jsonData.convert<Trip>()

                        Results.Success(
                            data = arrayListOf(trip).filterNotNull() as ArrayList<Trip>,
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

    suspend fun updateTripDetails(
        driver: Driver,
        localTrip: LocalTrip
    ): Results {

        return try {
            withContext(Dispatchers.IO) {
                //1. first write to the repository
                val requestBody = FormBody.Builder()
                    .add("surname", driver.lastName)
                    .add("passcode", driver.passCode)
                    .add("trip", localTrip.trip.toJson())
                val req = requestBody.build()

                val request = Request.Builder()
                    .url("$baseUrl/trip_update")
                    .post(req)
                    .build()

                val results =
                    client.newCall(request).execute()//wait for the results from the SERVER
                val data = results.body?.string()
                val jsonTree = JsonParser.parseString(data).asJsonObject
                val writeResp = jsonTree.get("Status")?.toString()?.replace("\"", "")

                if (writeResp == "Success") {
                    val jsonData = jsonTree.get("data").toString()
                    val trip = jsonData.convert<Trip>()
                    localTrip.trip = trip
                    localTrip.awaitingNetwork = false
                    tripDao.updateTrip(localTrip = localTrip)

                    Results.Success(
                        data = arrayListOf(localTrip),
                        code = Results.Success.CODE.UPDATE_SUCCESS
                    )
                } else Results.Error(AbstractModel.ServerException())
            }
        } catch (e: Exception) {
            try {
                localTrip.awaitingNetwork = true
                tripDao.updateTrip(localTrip)

            } catch (e: Exception) {
                //should not come here
                e.printStackTrace()
                Results.Error(e)
            }
            e.printStackTrace()
            Results.Success(
                data = arrayListOf(localTrip),
                code = Results.Success.CODE.AWAITING_NETWORK
            )
        }
    }

    suspend fun loadTripInfo(driver: Driver): Results {
        return try {
            withContext(Dispatchers.IO) {
                val deferredRecords = listOf(
                    async { tripDao.loadCurrentTrip() },
                    async { tripDao.loadCurrentTruck() }
                )
                val data = deferredRecords.awaitAll().filterNotNull() as ArrayList<AbstractModel>
                var responseArray: ArrayList<AbstractModel> = data

                val localTrip = data.filterIsInstance<LocalTrip>().firstOrNull()
                val truck = data.filterIsInstance<Truck>().firstOrNull()

                localTrip?.let {
                    if (it.id == null && it.awaitingNetwork) {
                        //trip create at backend - never written to db
                        val results = createNewTrip(driver, localTrip)
                        responseArray = if (results is Results.Success<*>)
                            arrayListOf(
                                results.data!!.first(),
                                truck
                            ).filterNotNull() as ArrayList<AbstractModel>
                        else { //should normally not come here
                            arrayListOf(truck).filterNotNull() as ArrayList<AbstractModel>
                        }

                    } else if (it.awaitingNetwork && it.id != null) {
                        //trip update at backend
                        val results = updateTripDetails(driver, localTrip)
                        responseArray = if (results is Results.Success<*>)
                            arrayListOf(results.data!!.first(), truck) as ArrayList<AbstractModel>
                        else { //should normally not come here
                            arrayListOf(truck) as ArrayList<AbstractModel>
                        }
                    }
                }
                Results.Success(data = responseArray, code = Results.Success.CODE.LOAD_SUCCESS)
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
            e.printStackTrace()
            "0.0"
        }
    }

    suspend fun cancelCurrentTrip(driver: Driver, localTrip: LocalTrip): Results {
        //1. first write to the repository
        val requestBody = FormBody.Builder()
            .add("passcode", driver.passCode)
            .add("surname", driver.lastName)
            .add("trip", localTrip.trip.toJson())
            .build()

        val request = Request.Builder()
            .url("$baseUrl/trip_delete")
            .post(requestBody)
            .build()
        return try {
            withContext(Dispatchers.IO) {

                if (localTrip.trip?.id != null) {
                    //1. if persisted to backend
                    val results =
                        client.newCall(request).execute()//wait for the results from the SERVER
                    val data = results.body?.string()
                    val jsonTree = JsonParser.parseString(data).asJsonObject
                    val writeResp = jsonTree.get("Status")?.toString()?.replace("\"", "")
                    if (writeResp == "Success") {
                        tripDao.clearTripTable()
                        tripDao.clearJobCardTable()
                        Results.Success<Trip>(code = Results.Success.CODE.DELETE_SUCCESS)
                    } else Results.Error(AbstractModel.ServerException())
                } else {
                    //never persisted to backend
                    tripDao.clearTripTable()
                    tripDao.clearJobCardTable()
                    Results.Success<Trip>(code = Results.Success.CODE.DELETE_SUCCESS)
                }
            }
        } catch (e: Exception) {
            Results.Error(e)
        }
    }

    suspend fun loadActiveTripsOnJobCard(driver: Driver, jobCardNo: String): Results {
        val url = "${baseUrl}/trip_on_jobCard?surname=${driver.lastName}&passcode=${driver.passCode}&jobCardNo=$jobCardNo"
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
                        val tripList = if (jsonData.isEmpty())
                            arrayListOf() else jsonData.convert<ArrayList<Trip>>()

                        Results.Success(
                            data = tripList,
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

@Database(
    entities = [Truck::class, LocalTrip::class, JobCard::class, Driver::class],
    version = 1,
    exportSchema = false
)
abstract class TripDatabase : RoomDatabase() {

    abstract fun tripDao(): CurrentTripDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: TripDatabase? = null

        fun getDatabase(context: Context): TripDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TripDatabase::class.java,
                    "Current Trip Database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

@Dao
interface CurrentTripDao {

    /** [Truck] room table ops */
    @Insert
    suspend fun insertTruck(truck: Truck)

    @Update
    suspend fun updateTruck(truck: Truck)

    @Query("delete from Truck")
    suspend fun clearTruckTable()

    @Query("select * from Truck limit 1")
    suspend fun loadCurrentTruck(): Truck?

    /** [Trip] room table ops */
    @Insert
    suspend fun insertTrip(localTrip: LocalTrip)

    @Update
    suspend fun updateTrip(localTrip: LocalTrip)

    @Query("delete from LocalTrip")
    suspend fun clearTripTable()

    @Query("select * from LocalTrip order by id desc limit 1")
    suspend fun loadCurrentTrip(): LocalTrip?

    @Query(value = "select * from LocalTrip")
    suspend fun loadAllTrips(): List<LocalTrip>?

    /** [Driver] room table ops */
    @Insert
    suspend fun insertDriver(driver: Driver)

    @Update
    suspend fun updateDriver(driver: Driver)

    @Query("delete from Driver")
    suspend fun clearDriverTable()

    @Query("select * from Driver limit 1")
    suspend fun loadCurrentDriver(): Driver?

    /** [JobCard] room table ops */
    @Insert
    suspend fun insertJobCard(jobCard: JobCard)

    @Update
    suspend fun updateJobCard(jobCard: JobCard)

    @Query("delete from JobCard")
    suspend fun clearJobCardTable()

    @Query("select * from JobCard limit 1")
    suspend fun loadCurrentJobCard(): JobCard?
}