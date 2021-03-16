package com.pet001kambala.namopscontainers.ui.account

import androidx.lifecycle.*
import com.pet001kambala.namopscontainers.repo.AccountRepo

class AccountViewModel: ViewModel() {

    private val accountRepo = AccountRepo()

    enum class AuthState {
        AUTHENTICATED, UNAUTHENTICATED, EMAIL_NOT_VERIFIED
    }

    val authState = MutableLiveData<AuthState>()

    private val userId = MutableLiveData<String>()

//    @ExperimentalCoroutinesApi
//    val currentAccount = userId.switchMap { userId ->
////        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
////            accountRepo.accountChangeListener(userId).collect {
////                if (it is Results.Success<*> && !it.data.isNullOrEmpty())
////                    emit(it.data.first() as Account)
////            }
////        }
//    }
}