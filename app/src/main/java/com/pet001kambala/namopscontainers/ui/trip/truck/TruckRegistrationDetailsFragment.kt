package com.pet001kambala.namopscontainers.ui.trip.truck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import com.pet001kambala.namopscontainers.databinding.FragmentTruckRegistrationBinding
import com.pet001kambala.namopscontainers.model.Truck
import com.pet001kambala.namopscontainers.ui.AbstractFragment
import com.pet001kambala.namopscontainers.ui.trip.AbstractTripDetailsFragment
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class TruckRegistrationDetailsFragment : AbstractTripDetailsFragment() {

    private lateinit var binding: FragmentTruckRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTruckRegistrationBinding.inflate(inflater, container, false)

        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tripModel.viewModelScope.launch {
            showProgressBar("Loading current truck...")
            val truck = tripModel.tripDao.loadCurrentTruck()
            endProgressBar()
            binding.truck = truck
        }
        binding.register.setOnClickListener {
            val observer = if (truck == null) {
                binding.truck = Truck()
                tripModel.addTruck(binding.truck!!)
            } else
                tripModel.updateTruck(binding.truck!!)

            observer.observe(viewLifecycleOwner) { results ->
                when (results) {
                    is Results.Loading -> showProgressBar("Just a moment...")
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