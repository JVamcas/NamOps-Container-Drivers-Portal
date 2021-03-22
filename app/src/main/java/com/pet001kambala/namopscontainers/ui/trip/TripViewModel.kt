package com.pet001kambala.namopscontainers.ui.trip

import android.app.Application
import androidx.lifecycle.*
import com.pet001kambala.namopscontainers.model.Driver
import com.pet001kambala.namopscontainers.model.Trip
import com.pet001kambala.namopscontainers.model.Truck
import com.pet001kambala.namopscontainers.repo.TripDatabase
import com.pet001kambala.namopscontainers.repo.TripRepo
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.ExperimentalCoroutinesApi

class TripViewModel(app: Application) : AndroidViewModel(app) {

    private val tripRepo = TripRepo()
    private var _currentTrip = MutableLiveData<Trip>()
    private var _currentTruck = MutableLiveData<Truck>()
    lateinit var oPLiveData: LiveData<Results>

    val currentTrip: LiveData<Trip> = _currentTrip
    val currentTruck: LiveData<Truck> = _currentTruck

    private val db by lazy { TripDatabase.getDatabase(app.baseContext) }
    val tripDao by lazy { db.tripDao() }


    @ExperimentalCoroutinesApi
    fun createNewTrip(driver: Driver, trip: Trip): LiveData<Results> {
        oPLiveData = liveData {
            emit(Results.Loading)
            try {
                val results = tripRepo.createNewTrip(driver.passCode, trip)
                if (results is Results.Success<*>)
                    _currentTrip.value = trip
                emit(results)
            } catch (e: Exception) {
                emit(Results.Error(e))
            }
        }
        return oPLiveData
    }

    private fun updateTruckDetails(truck: Truck) {
        _currentTrip.value = _currentTrip.value?.apply { //in case there's a trip active
            truckReg = truck.truckReg
            firstTrailerReg = truck.firstTrailerReg
            secondTrailerReg = truck.secondTrailerReg
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
}