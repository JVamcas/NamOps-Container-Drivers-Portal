package com.pet001kambala.namopscontainers.repo

import android.app.Application
import com.google.gson.JsonParser
import com.pet001kambala.namopscontainers.model.AbstractModel
import com.pet001kambala.namopscontainers.model.Auth
import com.pet001kambala.namopscontainers.model.Driver
import com.pet001kambala.namopscontainers.model.Trip
import com.pet001kambala.namopscontainers.utils.Const.Companion.baseUrl
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.convert
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception
import java.util.concurrent.Flow

class AccountRepo(private val app: Application) {

    val tripDao by lazy { TripDatabase.getDatabase(app).tripDao() }

    suspend fun login(auth: Auth? = null): Results {

        return try {
            withContext(Dispatchers.IO) {

                if (auth == null) {
                    val driver = tripDao.loadCurrentDriver()
                    val data = if (driver == null) arrayListOf() else arrayListOf(driver)
                    Results.Success(data = data, code = Results.Success.CODE.LOAD_SUCCESS)
                } else {
                    tripDao.clearDriverTable()//remove any driver who was there

                    val url =
                        "$baseUrl/drivers?surname=${auth.surname}&passcode=${auth.passcode}"
                    val client = OkHttpClient.Builder().build()
                    val request = Request.Builder()
                        .url(url)
                        .build()

                    val results = client.newCall(request).execute()//wait for the results from api
                    val data = results.body?.string()

                    val jsonTree = JsonParser.parseString(data).asJsonObject

                    when (jsonTree.get("Status").toString().replace("\"", "")) {
                        "Success" -> {
                            val jsonData = jsonTree.get("data").toString()
                            val driver = jsonData.convert<Driver>()

                            tripDao.insertDriver(driver)
                            Results.Success(
                                data = arrayListOf(driver).filterNotNull() as ArrayList<Driver>,
                                code = Results.Success.CODE.LOAD_SUCCESS
                            )
                        }
                        "Invalid Auth" -> Results.Error(AbstractModel.InvalidAuthCredException())

                        else -> Results.Error(AbstractModel.ServerException())
                    }
                }
            }
        } catch (e: Exception) {
            Results.Error(e)
        }
    }
}