package com.pet001kambala.namopscontainers.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.pet001kambala.namopscontainers.MainActivity
import com.pet001kambala.namopscontainers.R
import com.pet001kambala.namopscontainers.databinding.ProgressbarBinding
import com.pet001kambala.namopscontainers.databinding.WarningDialogBinding
import com.pet001kambala.namopscontainers.model.Driver
import com.pet001kambala.namopscontainers.model.TripStatus
import com.pet001kambala.namopscontainers.model.Truck
import com.pet001kambala.namopscontainers.ui.account.AccountViewModel
import com.pet001kambala.namopscontainers.ui.account.LoginFragment
import com.pet001kambala.namopscontainers.ui.home.HomeFragment
import com.pet001kambala.namopscontainers.ui.trip.AbstractTripDetailsFragment
import com.pet001kambala.namopscontainers.ui.trip.PickUpContainerFragment
import com.pet001kambala.namopscontainers.ui.trip.TripViewModel
import com.pet001kambala.namopscontainers.utils.Const
import com.pet001kambala.namopscontainers.utils.Const.Companion.REQUEST_CHECK_SETTINGS
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.isInvalid
import com.pet001kambala.namopscontainers.utils.Results
import com.pet001kambala.namopscontainers.utils.Results.Error.CODE.*
import com.pet001kambala.namopscontainers.utils.Results.Success.CODE.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import java.lang.Exception
import java.util.concurrent.atomic.AtomicBoolean


abstract class AbstractFragment : Fragment() {


    private var mDialog: Dialog? = null
    lateinit var navController: NavController
    private lateinit var mProgressbarBinding: ProgressbarBinding
    val accountModel: AccountViewModel by activityViewModels()
    var driver: Driver? = null
    var truck: Truck? = null
    val tripModel: TripViewModel by activityViewModels()



    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tripModel.currentTruck.observe(requireActivity()) {
            it?.let {
                truck = it
            }
        }




//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

//        requestPermissionLauncher =
//            registerForActivityResult(
//                ActivityResultContracts.RequestPermission()
//            ) { isGranted: Boolean ->
//                when {
//                    isGranted -> {
//                        // Permission is granted. Continue the action or workflow in your
//                        // app.
//                        parseLocationTask()
//                    }
//                    else -> {
//                        // Explain to the user that the feature is unavailable because the
//                        // features requires a permission that the user has denied. At the
//                        // same time, respect the user's decision. Don't link to system
//                        // settings in an effort to convince the user to change their
//                        // decision.
//                        print("Hellow")
//                    }
//                }
//            }
    }


    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBackClicks()

        val navHostFragment =
            (requireActivity() as MainActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        driver = accountModel.currentDriver.value
        accountModel.currentDriver.observe(viewLifecycleOwner) {
            driver = it
            if (it.isInvalid() && this@AbstractFragment !is LoginFragment)
                navController.navigate(R.id.action_global_loginFragment)

        }

        when {
            driver.isInvalid() && this@AbstractFragment !is LoginFragment ->
                navController.navigate(
                    R.id.action_global_loginFragment
                )
            else -> {
                driver?.let {
                    if (this is AbstractTripDetailsFragment || this is HomeFragment) {
                        tripModel.loadCurrentTrip(it).observe(requireActivity()) { result ->
                            when (result) {
                                is Results.Loading -> showProgressBar("Loading current trip info...")
                                is Results.Success<*> -> {
                                    endProgressBar()
                                    if (truck == null)
                                        navController.navigate(R.id.action_newTripFragment_to_updateTruckDetailsFragment)
                                }
                                else -> {
                                    endProgressBar()
                                    parseRepoResults(result)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

//    @SuppressLint("MissingPermission")
//    private fun parseLocationTask() {
//        task.addOnSuccessListener {
//            GlobalScope.launch {
//                val loc = fusedLocationClient.lastLocation.await()
//                location = loc?.let { "${it.latitude} ${it.longitude}" }
//                showToast("Location is ${location}")
//            }
//        }
//
//        task.addOnFailureListener { exception ->
//            if (exception is ResolvableApiException) {
//                // Location settings are not satisfied, but this can be fixed
//                // by showing the user a dialog.
//                try {
//                    // Show the dialog by calling startResolutionForResult(),
//                    // and check the result in onActivityResult().
//                    exception.startResolutionForResult(
//                        requireActivity(),
//                        REQUEST_CHECK_SETTINGS
//                    )
//                } catch (sendEx: IntentSender.SendIntentException) {
//                    // Ignore the error.
//                    sendEx.printStackTrace()
//                }
//            }
//        }
//    }
//
//    fun getDeviceCurrentLocation() {
//        when (PackageManager.PERMISSION_GRANTED) {
//            ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) -> {
//                showToast("Show location called.")
//                parseLocationTask()
//            }
//
//            else -> {
//                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//            }
//        }
//    }

    protected open fun showProgressBar(message: String) {
        if (mDialog == null || mDialog?.isShowing == false) {
            mProgressbarBinding = ProgressbarBinding.inflate(layoutInflater, null, false)
            mProgressbarBinding.progressMsg.text = message

            mDialog = Dialog(requireContext(), android.R.style.Theme_Black)
            mDialog?.apply {

                window?.setGravity(Gravity.BOTTOM)
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                window?.setBackgroundDrawableResource(R.color.transparent)
                setContentView(mProgressbarBinding.root)
                setCancelable(false)
                show()
            }
        }
    }


    protected fun updateProgressBarMsg(msg: String) {
        mProgressbarBinding.progressMsg.text = msg
    }

    protected open fun endProgressBar() {
        mDialog?.dismiss()
    }

    private fun handleBackClicks() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackClick()
                }
            })
    }

    protected open fun onBackClick() {
        navController.popBackStack()
    }

    fun showToast(msg: String?) {
        requireActivity().runOnUiThread {
            context?.let {
                Toast.makeText(it, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    protected fun showWarningDialog(warningTxt: String?, mListener: WarningDialogListener) {
        with(WarningDialogBinding.inflate(layoutInflater, null, false))
        {
            val dialog = AlertDialog.Builder(requireContext()).let {
                it.setView(root)
                it.create()
            }.apply { show() }
            title.text = warningTxt
            val okBtnClicked = AtomicBoolean(false)
            okBtn.setOnClickListener {
                okBtnClicked.set(true)
                dialog.dismiss()
                mListener.onOkWarning()
            }
            cancelBtn.setOnClickListener { dialog.dismiss() }
            dialog.setOnDismissListener { if (!okBtnClicked.get()) mListener.onCancelWarning() }
        }
    }

    protected fun parseRepoResults(mResults: Results?) {
        if (mResults is Results.Success<*>) {
            when (mResults.code) {
                AUTH_SUCCESS -> {
                }
                WRITE_SUCCESS -> showToast(" Registration success.")
                UPDATE_SUCCESS -> showToast("Updated success.")
                LOGOUT_SUCCESS -> showToast("Logout successfully!")
                DELETE_SUCCESS -> showToast("Deleted successfully.")
                VERIFICATION_EMAIL_SENT -> showToast("Verification email sent.")
            }
        } else if (mResults is Results.Error) {
            when (mResults.code) {
                PERMISSION_DENIED -> showToast("Err: Permission denied!")
                NETWORK -> showToast("Err: No internet connection!")
                ENTITY_EXISTS -> showToast("Err: Duplicate.")
                AUTH -> showToast("Err: Authentication.")
                NO_RECORD -> showToast("Err: No record found for your search.")
                NO_ACCOUNT -> showToast("Err: No record found for your search.")
                NO_SUCH_USER -> showToast("Err: No account with such email.")
                DUPLICATE_ACCOUNT -> showToast("Err: Account already exist.")
                INCORRECT_EMAIL_PASSWORD_COMBO -> showToast("Err: Incorrect email or password.")
                INVALID_AUTH_CODE -> showToast("Err: Incorrect authentication code.")
                PHONE_VERIFICATION_CODE_EXPIRED -> showToast("Err: Verification code expired.")
                INVALID_CON_AUTH -> showToast("Err: Incorrect surname or password.")
                CONNECTION_TIMEOUT -> showToast("Err: Connection timeout.")
                SERVER_ERR -> showToast("Err: Server error.")
                NO_CONNECTION -> showToast("Err: No connection!")
                NULL_CONNECTION_DETAILS -> showToast("Err: Configure connection first.")
                SEEN -> {/*ignore results we have seen it*/
                }
                else -> showToast("Err: Unknown error!")
            }
        }
    }

    fun LiveData<Results>.observeOnce(lifecycleOwner: LifecycleOwner, observer: (Results) -> Unit) {
        observe(lifecycleOwner, object : Observer<Results> {
            override fun onChanged(results: Results) {
                if (results is Results.Success<*> || results is Results.Error)
                    removeObserver(this)
                observer(results)
            }
        })
    }

    interface WarningDialogListener {
        /***
         * Called when user Ok the delete Op
         */
        fun onOkWarning()

        /***
         * Called when user explicitly cancelled the op or when dialog dismissed
         * by touching elsewhere in the device screen
         */
        fun onCancelWarning()
    }
}