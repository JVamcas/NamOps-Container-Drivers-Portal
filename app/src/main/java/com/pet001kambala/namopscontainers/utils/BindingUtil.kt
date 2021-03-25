package com.pet001kambala.namopscontainers.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.pet001kambala.namopscontainers.model.JobCard
import com.pet001kambala.namopscontainers.model.Trip
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.isValidEmail
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.isValidMobile
import com.skydoves.powerspinner.PowerSpinnerView

import com.squareup.picasso.Picasso

class BindingUtil {

    companion object {
        @JvmStatic
        @BindingAdapter(value = ["errorMsg", "editContent"])
        fun emptyEdit(mEditText: EditText, errorMsg: String?, value: String?) {
            mEditText.error = if (value.isNullOrEmpty()) errorMsg
            else null
        }

        @JvmStatic
        @BindingAdapter(value = ["errorMsg", "truck_reg"])
        fun validateTruckReg(mEditText: EditText, errorMsg: String?, truckReg: String?) {
            mEditText.error = if (truckReg.isNullOrEmpty()) errorMsg
            else null
        }

        @JvmStatic
        @BindingAdapter(value = ["errorMsg", "plateNumber", "isSecondTrailer"])
        fun validateTrailerPlateNumber(
            mEditText: EditText,
            errorMsg: String?,
            plateNumber: String?,
            isSecondTrailer: Boolean = false
        ) {

        }

        @JvmStatic
        @BindingAdapter(value = ["containerNo", "jobCard", "containerIndex", "trip"])
        fun validateSetJobCardNo(
            mEditText: MaterialAutoCompleteTextView,
            containerNo: String?,
            jobCard: JobCard?,
            containerIndex: Int?,
            trip: Trip
        ) {
            val containerOnJobCard =
                jobCard?.jobCardItemList?.any { it.containerNo == containerNo }

            val jobCardNo = if(containerOnJobCard == true) jobCard.jobCardNo else Const.DefaultJobCardNo

            when (containerIndex) {
                1 -> trip.container1JobCardId = jobCardNo

                2 -> trip.container2JobCardId = jobCardNo

                3 -> trip.container3JobCardId = jobCardNo
            }

            mEditText.error =
                if (containerNo?.length == 11) null else "Invalid container number."
        }


        @JvmStatic
        @BindingAdapter(value = ["accountName", "cellphone"])
        fun validatePhoneRegistration(
            mButton: MaterialButton,
            accountName: String?,
            companyName: String?,
            companyNumber: String?,
            cellphone: String?
        ) {
            mButton.isEnabled =
                isValidSelection(arrayListOf(accountName, companyName, companyNumber))
                        && isValidMobile(cellphone)
        }

        @JvmStatic
        @BindingAdapter(value = ["errorMsg", "emailAddressUpdate"])
        fun validateEmailUpdate(mEditText: EditText, errorMsg: String?, emailAddress: String?) {
            mEditText.error =
                if (isValidEmail(emailAddress) || emailAddress.isNullOrEmpty()) null else errorMsg
        }

        @JvmStatic
        @BindingAdapter(value = ["errorMsg", "cellphoneUpdate"])
        fun validateCellUpdate(mEditText: EditText, errorMsg: String?, cellphone: String?) {
            mEditText.error =
                if (isValidMobile(cellphone) || cellphone.isNullOrEmpty()) null else errorMsg
        }

        @JvmStatic
        @BindingAdapter(value = ["accountName", "cellphone", "email_address"])
        fun validateProfileUpdate(
            mButton: MaterialButton,
            accountName: String?,
            cellphone: String?,
            email_address: String?
        ) {
            mButton.isEnabled =
                isValidSelection(arrayListOf(accountName))
                        && (email_address.isNullOrEmpty() || isValidEmail(email_address))
                        && (cellphone.isNullOrEmpty() || isValidMobile(cellphone))
        }


        @JvmStatic
        @BindingAdapter(value = ["errorMsg", "idMailCell"])
        fun validateIDMailCell(
            mEditText: TextInputEditText,
            errorMsg: String?,
            idMailCell: String?
        ) {

            mEditText.error =
                if (idMailCell.isNullOrEmpty() || isValidMobile(idMailCell) || isValidEmail(
                        idMailCell
                    )
                )
                    null else errorMsg
        }

        @JvmStatic
        @BindingAdapter(value = ["password"])
        fun validatePassword(mEditText: EditText, password: String?) {
            mEditText.error =
                if (password.isNullOrEmpty() || password.length >= 8) null else
                    "Password should be at least 8 characters long."
        }

        @JvmStatic
        @BindingAdapter(value = ["password", "confirmPassword"])
        fun confirmPassword(mEditText: EditText, password: String?, confirmPassword: String?) {
            mEditText.error =
                if (confirmPassword.isNullOrEmpty() || password != confirmPassword) "Passwords do not match." else null
        }

        @JvmStatic
        @BindingAdapter(value = ["password", "idMailCell"], requireAll = false)
        fun isValidLogin(mButton: MaterialButton, password: String?, idMailCell: String?) {
            mButton.isEnabled =
                (password?.length ?: 0 >= 8 && isValidEmail(idMailCell) || isValidMobile(
                    idMailCell
                ) || idMailCell?.length ?: 0 == 11)
        }

        @JvmStatic
        @BindingAdapter(value = ["emailAddress", "password", "confirmPassword"])
        fun validateEmailRegistration(
            mButton: MaterialButton,
            emailAddress: String?,
            password: String?,
            confirmPassword: String?
        ) {
            mButton.isEnabled = isValidEmail(emailAddress)
                    || (isValidEmail(emailAddress) && password?.length ?: 0 >= 8 && confirmPassword == password)

        }

        @JvmStatic
        @BindingAdapter(value = ["emailAddress", "cellphone", "isEmail"])
        fun validateEmailCell(
            mEditText: TextInputEditText,
            emailAddress: String?,
            cellphone: String?,
            isEmail: Boolean = false
        ) {
            mEditText.error =
                if (isEmail) {
                    if (emailAddress.isNullOrEmpty() || !isValidEmail(emailAddress))
                        "Enter a valid email address."
                    else null
                } else if (cellphone.isNullOrEmpty() || !isValidMobile(cellphone))
                    "Enter valid phone number."
                else null
        }

        private fun isValidSelection(list: ArrayList<String?>): Boolean {
            return !list.any { it.isNullOrEmpty() || it.contains("Select") }
        }


        /***
         * Load image.png from provided url, transform it and set it into the imageview
         * @param mView the image.png view
         * @param default_icon default icon in case of error or when url is null
         * @param photoUrl the url: a base dir for device images else full url for online
         * @param size the required size of the image.png
         */
        @JvmStatic
        @BindingAdapter(value = ["viewId", "default_icon", "photoUrl", "size"])
        fun loadImage(
            mView: ImageView,
            viewId: Int?,
            @IdRes default_icon: Int,
            photoUrl: String?,
            size: Int
        ) {
            val filePath = ParseUtil.iconPath(Const.IMAGE_ROOT_PATH, (viewId ?: "").toString())
            val absPath = ParseUtil.findFilePath(mView.context, filePath)

            Picasso.get().invalidate(absPath)

            val creator =
                ImageTransformer.ImageUtil.requestCreator(
                    ImageTransformer.CircleTransformation,
                    absPath,
                    size,
                    default_icon
                )
            creator.into(mView)
        }


        @JvmStatic
        @BindingAdapter(value = ["email_address"])
        fun validateEmail(mEditText: EditText, email_address: String?) {
            mEditText.error =
                if (!email_address.isNullOrEmpty() && !isValidEmail(email_address)) "Invalid Email address." else null
        }

        abstract class TextChangeLister : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        }
    }
}