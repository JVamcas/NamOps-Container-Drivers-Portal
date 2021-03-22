package com.pet001kambala.namopscontainers.ui.trip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pet001kambala.namopscontainers.R
import com.pet001kambala.namopscontainers.ui.AbstractFragment


class UpdateTruckDetailsFragment : AbstractTripDetailsFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_truck_details, container, false)
    }


}