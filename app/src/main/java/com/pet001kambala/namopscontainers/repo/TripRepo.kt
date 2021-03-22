package com.pet001kambala.namopscontainers.repo

import android.app.Application
import android.content.Context
import androidx.room.*
import com.google.gson.JsonParser
import com.pet001kambala.namopscontainers.model.AbstractModel
import com.pet001kambala.namopscontainers.model.Driver
import com.pet001kambala.namopscontainers.model.Trip
import com.pet001kambala.namopscontainers.model.Truck
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.toJson
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.commons.csv.CSVFormat
import java.io.StringReader
import kotlin.math.round
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TripRepo(val app: Application) {

    //order
    //1. write -> local then backend
    //2. read -> backend then populate to local
    //3. if all fails read or write from/to local

    var baseUrl: String = "http://160.242.10.200:8081/namops_driver_portal"
    private val client = OkHttpClient.Builder().build()
    private val tripDao by lazy { TripDatabase.getDatabase(app).tripDao() }

    suspend fun createNewTrip(passCode: String, trip: Trip): Results {
        return try {
            //1. first write to room database
            tripDao.insertTrip(trip = trip)

            //2. then to repository
            val requestBody = FormBody.Builder()
                .add("passcode", passCode)
                .add("trip", trip.toJson())
                .build()

            val request = Request.Builder()
                .url("$baseUrl/trip")
                .post(requestBody)
                .build()

            withContext(Dispatchers.IO) {
                val results =
                    client.newCall(request).execute()//wait for the results from the SERVER
                val data = results.body?.string()
                val jsonTree = JsonParser.parseString(data).asJsonObject
                val writeResp = jsonTree.get("data").toString().replace("\"", "")

                if (writeResp == "Success")
                    Results.Success(data = arrayListOf(), code = Results.Success.CODE.WRITE_SUCCESS)
                else Results.Error(AbstractModel.ServerException())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Results.Error(e)
        }
    }

    suspend fun updateTripDetails(passCode: String,trip: Trip): Results{
        //todo if entry not exist at backed, insert it

        return try {
            //1. first write to room database
            tripDao.updateTrip(trip = trip)

            //2. then to repository
            val requestBody = FormBody.Builder()
                .add("passcode", passCode)
                .add("trip", trip.toJson())
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
                val writeResp = jsonTree.get("data").toString().replace("\"", "")

                if (writeResp == "Success")
                    Results.Success(data = arrayListOf(), code = Results.Success.CODE.UPDATE_SUCCESS)
                else Results.Error(AbstractModel.ServerException())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Results.Error(e)
        }
    }

    suspend fun loadTripInfo(): Results {
        return try {
            withContext(Dispatchers.IO) {
                val deferredRecords = listOf(
                     //TODO should be loaded from the backend unless if there is no network
                    // TODO then saved to local repo

                    async { tripDao.loadCurrentTrip() },
                    async { tripDao.loadCurrentTruck() }
                )
                val data = deferredRecords.awaitAll().filterNotNull() as ArrayList<AbstractModel>
                Results.Success(code = Results.Success.CODE.LOAD_SUCCESS,data = data)
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

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [Truck::class, Trip::class, Driver::class], version = 1, exportSchema = false)
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
    @Insert
    suspend fun insertTruck(truck: Truck)

    @Update
    suspend fun updateTruck(truck: Truck)

    @Query("delete from Truck")
    suspend fun clearTruckTable()

    @Query("select * from Truck limit 1")
    suspend fun loadCurrentTruck(): Truck?

    // trip room table ops

    @Insert
    suspend fun insertTrip(trip: Trip)

    @Update
    suspend fun updateTrip(trip: Trip)

    @Query("delete from Trip")
    suspend fun clearTripTable()

    @Query("select * from Trip limit 1")
    suspend fun loadCurrentTrip(): Trip?

    // driver room table ops
    @Insert
    suspend fun insertDriver(driver: Driver)

    @Update
    suspend fun updateDriver(driver: Driver)

    @Query("delete from Driver")
    suspend fun clearDriverTable()

    @Query("select * from Driver limit 1")
    suspend fun loadCurrentDriver(): Driver?
}