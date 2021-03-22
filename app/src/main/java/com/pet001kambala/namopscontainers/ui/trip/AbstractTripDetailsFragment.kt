package com.pet001kambala.namopscontainers.ui.trip

import android.os.Bundle
import android.view.View
import androidx.lifecycle.viewModelScope
import com.pet001kambala.namopscontainers.R
import com.pet001kambala.namopscontainers.model.Trip
import com.pet001kambala.namopscontainers.ui.AbstractFragment
import com.pet001kambala.namopscontainers.utils.Const
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.convert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

abstract class AbstractTripDetailsFragment : AbstractFragment() {
    lateinit var trip: Trip

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trip = Trip(driver = driver)
        arguments?.let {
            val json = it.getString(Const.TRIP)
            json?.let {
                trip = it.convert()
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tripModel.viewModelScope.launch {
            if (truck == null) {
                showProgressBar("Loading current truck...")
                val truck = tripModel.tripDao.loadCurrentTruck()
                endProgressBar()
                this@AbstractTripDetailsFragment.truck = truck
                if (truck == null)
                    navController.navigate(R.id.action_newTripFragment_to_updateTruckDetailsFragment)
            }
        }
    }
}