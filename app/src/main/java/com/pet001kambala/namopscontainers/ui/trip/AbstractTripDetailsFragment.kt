package com.pet001kambala.namopscontainers.ui.trip

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.pet001kambala.namopscontainers.R
import com.pet001kambala.namopscontainers.model.LocalTrip
import com.pet001kambala.namopscontainers.model.Trip
import com.pet001kambala.namopscontainers.ui.AbstractFragment
import com.pet001kambala.namopscontainers.utils.Const
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.convert
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

abstract class AbstractTripDetailsFragment : AbstractFragment() {
    //    lateinit var localTrip: LocalTrip


    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        localTrip = LocalTrip().apply { trip = Trip().also { it.driver = driver } }
//        arguments?.let {
//            val json = it.getString(Const.TRIP)
//            json?.let {
//                localTrip = it.convert()
//            }
//        }
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }


}