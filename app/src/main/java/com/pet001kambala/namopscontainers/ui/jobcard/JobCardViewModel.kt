package com.pet001kambala.namopscontainers.ui.jobcard

import androidx.lifecycle.*
import com.pet001kambala.namopscontainers.repo.JobCardRepo
import com.pet001kambala.namopscontainers.ui.jobcard.JobCardViewModel.Repo.jobCardRepo
import com.pet001kambala.namopscontainers.utils.Results
import java.lang.Exception

class JobCardViewModel : ViewModel() {

    object Repo {
        val jobCardRepo = JobCardRepo()
    }
//
//    private val driverId = MutableLiveData<Int>()
//
//    val preAssignedJobCards = driverId.switchMap { driverId ->
//        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
//            emit(Results.Loading)
//            try {
//                emit(jobCardRepo.loadPreAssignedJobCards(driverId))
//            } catch (e: Exception) {
//                e.printStackTrace()
//                emit(Results.Error(e))
//            }
//        }
//    }
//
//    val unAssignedJobCards = liveData {
//        emit(Results.Loading)
//        try {
//            emit(jobCardRepo.loadUnAssignedJobCards())
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emit(Results.Error(e))
//        }
//    }



//    val allIncompleteJobCards = liveData {
//        emit(Results.Loading)
//        try {
//            emit(jobCardRepo.loadAllJobCardItemsOnIncmpleteJobCards())
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emit(Results.Error(e))
//        }
//    }
}