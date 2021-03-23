package com.pet001kambala.namopscontainers.ui.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pet001kambala.namopscontainers.databinding.FragmentWeighEmptyTruckBinding
import com.pet001kambala.namopscontainers.model.TripStatus
import com.pet001kambala.namopscontainers.utils.DateUtil
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.copyOf
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

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

        binding.trip = localTrip.trip

        binding.register.setOnClickListener {

            localTrip.trip!!.dateWeightBridgeEmpty = DateUtil.localDateToday()
            val localTripCopy = localTrip.copyOf()!!
            localTripCopy.trip!!.tripStatus = TripStatus.PICK_UP

            tripModel.updateTripDetails(driver, localTripCopy).observe(viewLifecycleOwner) { results ->
                when (results) {
                    is Results.Loading -> showProgressBar("Saving empty-truck weight...")
                    is Results.Success<*> -> {
                        endProgressBar()
                        showToast("Saved.")
                        navController.popBackStack()
                    }
                    else -> {
                        endProgressBar()
                        parseRepoResults(results)
                    }
                }
            }
        }
    }
}