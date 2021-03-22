package com.pet001kambala.namopscontainers.ui.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pet001kambala.namopscontainers.databinding.FragmentWeighEmptyTruckBinding
import com.pet001kambala.namopscontainers.utils.DateUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi

class WeighEmptyTruckFragment : AbstractTripDetailsFragment() {

    private lateinit var binding: FragmentWeighEmptyTruckBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWeighEmptyTruckBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.trip = trip

        binding.register.setOnClickListener {
            trip.dateWeightBridgeEmpty = DateUtil.localDateToday()

        }
    }
}