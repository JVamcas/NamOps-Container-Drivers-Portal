package com.pet001kambala.namopscontainers.ui.trip

import android.app.Application
import androidx.lifecycle.*
import com.pet001kambala.namopscontainers.model.*
import com.pet001kambala.namopscontainers.repo.TripDatabase
import com.pet001kambala.namopscontainers.repo.TripRepo
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.ExperimentalCoroutinesApi

class TripViewModel(app: Application) : AndroidViewModel(app) {

    private val tripRepo = TripRepo(app)
    private var _currentTrip = MutableLiveData<LocalTrip>()
    private var _currentTruck = MutableLiveData<Truck>()
    lateinit var oPLiveData: LiveData<Results>

    val currentLocalTrip: LiveData<LocalTrip> = _currentTrip
    val currentTruck: LiveData<Truck> = _currentTruck

    private val db by lazy { TripDatabase.getDatabase(app.baseContext) }
    val tripDao by lazy { db.tripDao() }


    @ExperimentalCoroutinesApi
    fun createNewTrip(driver: Driver, localTrip: LocalTrip): LiveData<Results> {
        oPLiveData = liveData {
            emit(Results.Loading)
            try {
                val results = tripRepo.createNewTrip(driver.passCode, localTrip)
                if (results is Results.Success<*>) {
                    val data = results.data?.firstOrNull()
                    _currentTrip.value = data as? LocalTrip
                }
                emit(results)
            } catch (e: Exception) {
                emit(Results.Error(e))
            }
        }
        return oPLiveData
    }

    @ExperimentalCoroutinesApi
    fun updateTripDetails(driver: Driver, localTrip: LocalTrip): LiveData<Results> {
        oPLiveData = liveData {
            emit(Results.Loading)
            try {
                val results = tripRepo.updateTripDetails(driver.passCode, localTrip)
                if (results is Results.Success<*>) {
                    val data = results.data?.firstOrNull()
                    _currentTrip.value = data as? LocalTrip
                }
                emit(results)
            } catch (e: Exception) {
                emit(Results.Error(e))
            }
        }
        return oPLiveData
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
        oPLiveData = liveData {
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
        return oPLiveData
    }

    fun updateTruck(truck: Truck): LiveData<Results> {
        oPLiveData = liveData {
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
        return oPLiveData
    }

    fun loadCurrentTrip(driver: Driver): LiveData<Results> {
        oPLiveData = liveData {
            emit(Results.Loading)
            try {
                val results = tripRepo.loadTripInfo(driver.passCode)
                if (results is Results.Success<*>) {
                    if (!results.data.isNullOrEmpty()) {
                        _currentTrip.value =
                            results.data.filterIsInstance<LocalTrip>().firstOrNull()
                        _currentTruck.value = results.data.filterIsInstance<Truck>().firstOrNull()
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
     * 1. set trip to null,
     * 2. remove [LocalTrip] from room
     * 3. set JobCardItem completed in backend
     */
    fun completeTrip(driver: Driver,jobCard: JobCard): LiveData<Results>{
        oPLiveData = liveData {
            emit(Results.Loading)
            try {

                val jobCardItemList = jobCard.jobCardItemList



            } catch (e: Exception) {
                Results.Error(e)
            }
        }
        return oPLiveData
    }
}