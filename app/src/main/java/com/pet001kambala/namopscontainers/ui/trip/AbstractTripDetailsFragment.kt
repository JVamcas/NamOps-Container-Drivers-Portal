package com.pet001kambala.namopscontainers.ui.trip

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.pet001kambala.namopscontainers.ui.AbstractFragment
import com.pet001kambala.namopscontainers.utils.Const
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.atomic.AtomicInteger

abstract class AbstractTripDetailsFragment : AbstractFragment() {


    lateinit var fusedLocationClient: FusedLocationProviderClient
    var location: String? = null
    private lateinit var task: Task<LocationSettingsResponse>
    var mLocationEnabled = false //whether or not the location settings are enabled
    var mLocationPermissionGranted =
        false //whether or not the permission to access the device location has been granted

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }


    private fun verifyLocationPermissionGranted() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                mLocationPermissionGranted = true
                verifyLocationSettingsSatisfied()
            }
            else -> {
                mLocationPermissionGranted = false
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    Const.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun verifyLocationSettingsSatisfied() {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(requireActivity())
        task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {

            GlobalScope.launch {
                val loc = fusedLocationClient.lastLocation.await()
                location = loc.let { "${loc?.latitude} ${loc?.longitude}" }
            }
        }

        task.addOnFailureListener { exception ->
            mLocationEnabled = false
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(
                        requireActivity(),
                        Const.REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                    sendEx.printStackTrace()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            Const.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                mLocationPermissionGranted = (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private val called = AtomicInteger(1)
    override fun onResume() {
        super.onResume()
        if(called.decrementAndGet() == 0) {
            if (!mLocationPermissionGranted) {//user might have revoked app permission access at this point
                verifyLocationPermissionGranted()
            } else
                verifyLocationSettingsSatisfied() //user might have switched off location at this point
        }
    }
}