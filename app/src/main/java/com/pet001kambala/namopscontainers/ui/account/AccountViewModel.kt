package com.pet001kambala.namopscontainers.ui.account

import android.app.Application
import androidx.lifecycle.*
import com.pet001kambala.namopscontainers.model.Auth
import com.pet001kambala.namopscontainers.model.Driver
import com.pet001kambala.namopscontainers.repo.AccountRepo
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.ExperimentalCoroutinesApi

class AccountViewModel(app: Application) : AndroidViewModel(app) {

    private val accountRepo = AccountRepo(app)

    private var _currentDriver = MutableLiveData<Driver>()
    val currentDriver: LiveData<Driver> = _currentDriver


    @ExperimentalCoroutinesApi
    fun login(auth: Auth? = null): LiveData<Results> {
        return liveData {
            emit(Results.Loading)
            try {
                val results = accountRepo.login(auth = auth)
                if (results is Results.Success<*>) {
                    val data = results.data?.firstOrNull()
                    _currentDriver.value = data as? Driver
                }
                emit(results)
            } catch (e: Exception) {
                emit(Results.Error(e))
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun signOut(): LiveData<Results> {
        return liveData {
            emit(Results.Loading)
            try {
                accountRepo.tripDao.clearDriverTable()
                _currentDriver.postValue(Driver(id = 0))
                emit(Results.Success<Driver>(code = Results.Success.CODE.LOGOUT_SUCCESS))
            } catch (e: Exception) {
                emit(Results.Error(e))
            }
        }
    }
}