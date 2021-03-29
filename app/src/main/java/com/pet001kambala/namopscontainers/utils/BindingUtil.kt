package com.pet001kambala.namopscontainers.utils

import android.text.Editable
import android.text.TextUtils
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
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.isValidContainerNo
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.isValidEmail
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.isValidMobile
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.isValidOdo
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.isValidPlateNo
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.isValidVehicleNo
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.isValidWeight

import com.squareup.picasso.Picasso
import java.util.regex.Pattern

class BindingUtil {

    companion object {
        @JvmStatic
        @BindingAdapter(value = ["errorMsg", "editContent"])
        fun emptyEdit(mEditText: EditText, errorMsg: String?, value: String?) {
            mEditText.error = if (value.isNullOrEmpty()) errorMsg
            else null
        }

        @JvmStatic
        @BindingAdapter(value = ["errorMsg", "truckWeight"])
        fun validateTruckWeight(mEditText: EditText, errorMsg: String?, truckWeight: Long?) {
            mEditText.error = if (!truckWeight.isValidWeight()) errorMsg
            else null
        }

        @JvmStatic
        @BindingAdapter(value = ["errorMsg", "truck_reg"])
        fun validateTruckReg(mEditText: EditText, errorMsg: String?, truckReg: String?) {
            mEditText.error = when {
                truckReg.isNullOrEmpty() || truckReg.isValidVehicleNo() -> null
                else -> errorMsg
            }
        }

        @JvmStatic
        @BindingAdapter(value = ["errorMsg", "plateNumber", "isSecondTrailer"])
        fun validateTrailerPlateNumber(
            mEditText: EditText,
            errorMsg: String?,
            plateNumber: String?,
            isSecondTrailer: Boolean = false
        ) {
            mEditText.error = when {
                !isSecondTrailer && plateNumber.isValidPlateNo() -> null
                isSecondTrailer && (plateNumber.isNullOrEmpty() || plateNumber.isValidPlateNo()) -> null
                else -> errorMsg
            }
        }

        @JvmStatic
        @BindingAdapter(value = ["startLocation", "truckOdo"])
        fun validateTripStart(
            mButton: MaterialButton,
            startLocation: String?,
            truckOdo: String?
        ) {

            mButton.isEnabled =
                !TextUtils.isEmpty(startLocation) && truckOdo.isValidOdo()

        }

        @JvmStatic
        @BindingAdapter(value = ["truckWeight"])
        fun validateTruckWeight(
            mButton: MaterialButton,
            truckWeight: Long?
        ) {

            mButton.isEnabled = truckWeight.isValidWeight()

        }


        @JvmStatic
        @BindingAdapter(value = ["truckReg", "firstPlate", "secondPlate"])
        fun validateTruckUpdate(
            mButton: MaterialButton,
            truckReg: String?,
            firstPlate: String?,
            secondPlate: String?
        ) {
            mButton.isEnabled =
                truckReg.isValidVehicleNo()
                        && firstPlate.isValidPlateNo()
                        && (TextUtils.isEmpty(secondPlate) || secondPlate.isValidPlateNo())
        }

        @JvmStatic
        @BindingAdapter(value = ["firstContainer", "secondContainer", "thirdContainer", "truckOdo", "locationName"])
        fun validateContainerPickUp(
            mButton: MaterialButton,
            firstContainer: String?,
            secondContainer: String?,
            thirdContainer: String?,
            truckOdo: String?,
            locationName: String?
        ) {
            val containers = arrayListOf(firstContainer, secondContainer, thirdContainer)
            mButton.isEnabled =
                containers.any { it.isValidContainerNo() } //one of the containers must be valid
                        && containers.all { TextUtils.isEmpty(it) || it.isValidContainerNo() } //if any container is set, it must be valid container number
                        && truckOdo.isValidOdo()
                        && !TextUtils.isEmpty(locationName)

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

            val jobCardNo = when {
                !containerNo.isValidContainerNo() -> null
                containerOnJobCard == true -> jobCard.jobCardNo
                else -> Const.DefaultJobCardNo
            }

            when (containerIndex) {
                1 -> trip.container1JobCardId = jobCardNo

                2 -> trip.container2JobCardId = jobCardNo

                3 -> trip.container3JobCardId = jobCardNo
            }

            mEditText.error =
                if (TextUtils.isEmpty(containerNo) || containerNo?.isValidContainerNo() == true) null else "Invalid container number."
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

    }
}