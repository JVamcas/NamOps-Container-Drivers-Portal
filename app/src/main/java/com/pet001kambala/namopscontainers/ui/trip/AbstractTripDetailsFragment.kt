package com.pet001kambala.namopscontainers.ui.trip

import android.os.Bundle
import com.pet001kambala.namopscontainers.model.Trip
import com.pet001kambala.namopscontainers.ui.AbstractFragment
import com.pet001kambala.namopscontainers.utils.Const
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.convert
import kotlinx.coroutines.ExperimentalCoroutinesApi

abstract class AbstractTripDetailsFragment: AbstractFragment() {

    lateinit var trip: Trip
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trip = Trip()
        arguments?.let {
            val json = it.getString(Const.TRIP)
            json?.let {
                trip = it.convert()
            }
        }
    }
}