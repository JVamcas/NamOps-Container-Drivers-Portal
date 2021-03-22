package com.pet001kambala.namopscontainers.ui.trip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.pet001kambala.namopscontainers.model.Driver
import com.pet001kambala.namopscontainers.model.Trip
import com.pet001kambala.namopscontainers.repo.TripRepo
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.ExperimentalCoroutinesApi

class TripViewModel : ViewModel() {

    private val tripRepo = TripRepo()
    lateinit var tripTransaction: LiveData<Results>
    val currentTrip = MutableLiveData<Trip>()


    @ExperimentalCoroutinesApi
    fun createNewTrip(driver: Driver, trip: Trip): LiveData<Results> {
        tripTransaction = liveData {
            emit(Results.Loading)
            try {
                val results = tripRepo.createNewTrip(driver.passCode, trip)
                if (results is Results.Success<*>)
                    currentTrip.value = trip
                emit(results)
            } catch (e: Exception) {
                emit(Results.Error(e))
            }
        }
        return tripTransaction
    }
}