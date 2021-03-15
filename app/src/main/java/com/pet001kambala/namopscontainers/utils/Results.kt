package com.pet001kambala.remotefiletransfer.utils

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code.ALREADY_EXISTS
import com.google.firebase.firestore.FirebaseFirestoreException.Code.PERMISSION_DENIED
import com.pet001kambala.remotefiletransfer.model.AbstractModel
import java.net.ConnectException
import java.net.SocketTimeoutException


/**
 * Represent results of an async operation
 */
sealed class Results {

    companion object {
        fun loading() = Loading
    }

    object Loading : Results()

    class Success<T : AbstractModel>(
        val data: ArrayList<T>? = null,
        val code: CODE
    ) : Results() {
        enum class CODE {
            WRITE_SUCCESS,
            UPDATE_SUCCESS,
            LOAD_SUCCESS,
            AUTH_SUCCESS,
            LOGOUT_SUCCESS,
            DELETE_SUCCESS,
            VERIFICATION_EMAIL_SENT,
            PASSWORD_RESET_LINK_SENT,
            PHONE_VERIFY_CODE_SENT,
            PHONE_VERIFY_SUCCESS,
            CONN_SWITCHED,
            CONN_SUCCESS
        }
        var dataSeen = false
        val dataIfNotSeen: ArrayList<T>?
            get() =
                if (!dataSeen) {
                    dataSeen = true
                    data
                } else null
    }

    class Error(error: Exception?) : Results() {
        enum class CODE {
            NETWORK,
            PERMISSION_DENIED,
            UNKNOWN,
            ENTITY_EXISTS,
            AUTH,
            NO_RECORD,
            NO_ACCOUNT,
            INVALID_AUTH_CODE,
            PHONE_VERIFICATION_CODE_EXPIRED,
            NO_SUCH_USER,
            DUPLICATE_ACCOUNT,
            INCORRECT_EMAIL_PASSWORD_COMBO,
            EMAIL_NOT_VERIFIED,
            NO_AUTH,
            INVALID_CON_AUTH,
            CONNECTION_TIMEOUT,
            SERVER_ERR,
            NO_CONNECTION,
            SEEN,
            NULL_CONNECTION_DETAILS
        }

        private val _code: CODE = when (error) {
            is AbstractModel.EntityExistException -> CODE.ENTITY_EXISTS
            is FirebaseAuthInvalidUserException -> CODE.NO_SUCH_USER
            is AbstractModel.InvalidPhoneAuthCodeException -> CODE.INVALID_AUTH_CODE
            is FirebaseAuthUserCollisionException -> CODE.DUPLICATE_ACCOUNT
            is FirebaseAuthException -> CODE.AUTH
            is FirebaseNetworkException -> CODE.NETWORK
            is AbstractModel.NoEntityException -> CODE.NO_RECORD
            is AbstractModel.NoAccountException -> CODE.NO_ACCOUNT
            is AbstractModel.PhoneVerificationCodeExpired -> CODE.PHONE_VERIFICATION_CODE_EXPIRED
            is AbstractModel.InvalidPasswordEmailException -> CODE.INCORRECT_EMAIL_PASSWORD_COMBO
            is AbstractModel.InvalidAuthCredException -> CODE.INVALID_CON_AUTH
            is SocketTimeoutException -> CODE.CONNECTION_TIMEOUT
            is AbstractModel.EmailNotVerifiedException -> CODE.EMAIL_NOT_VERIFIED
            is AbstractModel.ServerException -> CODE.SERVER_ERR
            is AbstractModel.NoConnectionException -> CODE.NO_CONNECTION
            is AbstractModel.NullConnectionDetailsException -> CODE.NULL_CONNECTION_DETAILS
            is ConnectException -> CODE.NO_CONNECTION
            is FirebaseFirestoreException -> {
                when (error.code) {
                    PERMISSION_DENIED -> CODE.PERMISSION_DENIED
                    ALREADY_EXISTS -> CODE.ENTITY_EXISTS
                    else -> CODE.UNKNOWN
                }
            }
            else -> CODE.UNKNOWN
        }
        var seen: Boolean = false
        val code: CODE
            get() =
                if (!seen) {
                    seen = true
                    _code
                } else CODE.SEEN
    }
}