package com.pet001kambala.namopscontainers.ui.trip

import android.app.Application
import androidx.lifecycle.*
import com.pet001kambala.namopscontainers.model.*
import com.pet001kambala.namopscontainers.repo.TripDatabase
import com.pet001kambala.namopscontainers.repo.TripRepo
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.*

class TripViewModel(app: Application) : AndroidViewModel(app) {

    private val tripRepo = TripRepo(app)
    private var _currentTrip = MutableLiveData<LocalTrip>()
    private var _currentTruck = MutableLiveData<Truck>()

    val currentLocalTrip: LiveData<LocalTrip> = _currentTrip
    val currentTruck: LiveData<Truck> = _currentTruck

    private val db by lazy { TripDatabase.getDatabase(app.baseContext) }
    val tripDao by lazy { db.tripDao() }

    @ExperimentalCoroutinesApi
    fun createNewTrip(driver: Driver, localTrip: LocalTrip): LiveData<Results> {
        return liveData {
            emit(Results.Loading)
            try {
                val results = tripRepo.createNewTrip(driver, localTrip)
                if (results is Results.Success<*>) {
                    val data = results.data?.firstOrNull()
                    _currentTrip.value = data as? LocalTrip
                }
                emit(results)
            } catch (e: Exception) {
                emit(Results.Error(e))
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun updateTripDetails(
        driver: Driver,
        localTrip: LocalTrip
    ): LiveData<Results> {
        return liveData {
            emit(Results.Loading)
            try {
                val results = tripRepo.updateTripDetails(
                    driver,
                    localTrip
                )
                if (results is Results.Success<*>) {
                    val data = results.data?.firstOrNull()
                    _currentTrip.value = data as? LocalTrip
                }
                emit(results)
            } catch (e: Exception) {
                emit(Results.Error(e))
            }
        }
    }

    private fun updateTruckDetails(truck: Truck) {
        _currentTrip.value = _currentTrip.value?.apply { //in case there's a trip active
            trip?.apply {
                truckReg = truck.truckReg
                firstTrailerReg = truck.firstTrailerReg
                secondTrailerReg = truck.secondTrailerReg
            }
        }
        //global truck
        _currentTruck.value = truck
    }

    fun addTruck(truck: Truck): LiveData<Results> {
        return liveData {
            emit(Results.Loading)
            try {
                tripDao.insertTruck(truck)
                emit(
                    Results.Success<Truck>(code = Results.Success.CODE.WRITE_SUCCESS)
                )
                updateTruckDetails(truck)
            } catch (e: Exception) {
                Results.Error(e)
            }
        }
    }

    fun updateTruck(truck: Truck): LiveData<Results> {
        return liveData {
            emit(Results.Loading)
            try {
                tripDao.updateTruck(truck)
                emit(
                    Results.Success<Truck>(code = Results.Success.CODE.UPDATE_SUCCESS)
                )
                updateTruckDetails(truck)
            } catch (e: Exception) {
                Results.Error(e)
            }
        }
    }

    @InternalCoroutinesApi
    fun loadTruckODO(truck: Truck): LiveData<Results> {
        return liveData {
            emit(Results.Loading)
            try {
                truck.odoMeter = tripRepo.findVehicleOdometer(truck.truckReg!!.toUpperCase())
                emit(Results.Success(data = arrayListOf(truck), Results.Success.CODE.LOAD_SUCCESS))
            } catch (e: Exception) {
                Results.Error(e)
            }
        }
    }

    fun loadCurrentTrip(driver: Driver): LiveData<Results> {
        val oPLiveData = liveData {
            emit(Results.Loading)
            try {
                val results = tripRepo.loadTripInfo(driver)
                if (results is Results.Success<*>) {
                    if (!results.data.isNullOrEmpty()) {
                        results.data.filterIsInstance<LocalTrip>().firstOrNull()?.let {
                            _currentTrip.value = it
                        }
                        results.data.filterIsInstance<Truck>().firstOrNull()?.let {
                            _currentTruck.value = it
                        }
                    }
                }
                emit(results)

            } catch (e: Exception) {
                Results.Error(e)
            }
        }
        return oPLiveData
    }

    fun loadCurrentJobCard(driver: Driver): LiveData<Results> {
        //1. load current trip - to get the job card no
        //2. load current job cards
        //3. recreate the jobcard, trip and save on database
        val oPLiveData = liveData {
            emit(Results.Loading)
            try {
                val results = tripRepo.loadTripInfo(driver)
                if (results is Results.Success<*>) {
                    if (!results.data.isNullOrEmpty()) {
                        results.data.filterIsInstance<LocalTrip>().firstOrNull()?.let {
                            _currentTrip.value = it
                        }
                        results.data.filterIsInstance<Truck>().firstOrNull()?.let {
                            _currentTruck.value = it
                        }
                    }
                }
                emit(results)

            } catch (e: Exception) {
                Results.Error(e)
            }
        }
        return oPLiveData
    }


    /**
     * 1. set JobCardItem completed in backend
     * 2. remove [LocalTrip] and [JobCard] from room database
     * 3. set [LocalTrip] to null,
     */
    fun completeTrip(
        driver: Driver, localTrip: LocalTrip
    ): LiveData<Results> {
        return liveData {
            emit(Results.Loading)
            try {
                val results = tripRepo.updateTripDetails(
                    driver = driver,
                    localTrip = localTrip
                )
                emit(
                    if (results is Results.Success<*>) {
                        if (results.code == Results.Success.CODE.UPDATE_SUCCESS) {
                            tripDao.clearJobCardTable()
                            tripDao.clearTripTable()
                        }
                        _currentTrip.value = LocalTrip()
                        results
                    } else results
                )

            } catch (e: Exception) {
                Results.Error(e)
            }
        }
    }

    fun cancelCurrentTrip(driver: Driver,localTrip: LocalTrip) : LiveData<Results>{
        return liveData {
            emit(Results.Loading)
            try {
                emit(tripRepo.cancelCurrentTrip(driver,localTrip))
            }
            catch (e: Exception){
                Results.Error(e)
            }
        }
    }
    fun loadActiveTripsOnJobCard(driver: Driver, jobCardNo: String) : LiveData<Results>{
        return liveData {
            emit(Results.Loading)
            try {
                emit(tripRepo.loadActiveTripsOnJobCard(driver,jobCardNo))
            }
            catch (e: Exception){
                Results.Error(e)
            }
        }
    }
}