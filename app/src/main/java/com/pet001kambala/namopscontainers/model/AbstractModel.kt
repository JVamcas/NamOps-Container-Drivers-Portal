package com.pet001kambala.namopscontainers.model

import androidx.annotation.Keep
import androidx.databinding.BaseObservable

@Keep
abstract class AbstractModel(
    @Transient
    open var id: Int? = null,
    @Transient
    open var photoUrl: String? = null
) : BaseObservable() {
    class EntityExistException : Exception()
    open class NoEntityException : Exception()
    class PhoneVerificationCodeExpired : Exception()
    class InvalidPasswordEmailException : Exception()
    class InvalidPhoneAuthCodeException : Exception()
    class NoAccountException : NoEntityException()
    class EmailNotVerifiedException : Exception()
    class NoAuthException : Exception()
    class InvalidAuthCredException : Exception()
    class ServerException : Exception()
    class NoConnectionException : Exception()
    class NullConnectionDetailsException : Exception()

    open fun data() = arrayListOf<Pair<String, String?>>()
}