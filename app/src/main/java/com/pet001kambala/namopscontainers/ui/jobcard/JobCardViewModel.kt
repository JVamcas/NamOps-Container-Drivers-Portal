package com.pet001kambala.namopscontainers.ui.jobcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.pet001kambala.namopscontainers.repo.JobCardRepo
import com.pet001kambala.namopscontainers.ui.jobcard.JobCardViewModel.Repo.jobCardRepo
import com.pet001kambala.namopscontainers.utils.Results
import java.lang.Exception

class JobCardViewModel : ViewModel() {
    object Repo {
        val jobCardRepo = JobCardRepo()
    }

    val preAssignedJobCards = liveData {
        emit(Results.Loading)
        try {
            val data = jobCardRepo.loadPreAssignedJobCards()
            emit(
                Results.Success(
                    data = data,
                    code = Results.Success.CODE.LOAD_SUCCESS
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Results.Error(e))
        }
    }

    val unAssignedJobCards = liveData {
        emit(Results.Loading)
        try {
            val data = jobCardRepo.loadUnAssignedJobCards()
            emit(
                Results.Success(
                    data = data,
                    code = Results.Success.CODE.LOAD_SUCCESS
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Results.Error(e))
        }
    }

    val allJobCards = liveData {
        emit(Results.Loading)
        try {
            val data = jobCardRepo.loadAllJobCards()
            emit(
                Results.Success(
                    data = data,
                    code = Results.Success.CODE.LOAD_SUCCESS
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Results.Error(e))
        }
    }
}