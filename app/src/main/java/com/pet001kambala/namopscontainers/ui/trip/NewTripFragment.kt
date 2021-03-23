package com.pet001kambala.namopscontainers.ui.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pet001kambala.namopscontainers.R
import com.pet001kambala.namopscontainers.databinding.FragmentNewTripBinding
import com.pet001kambala.namopscontainers.model.TripStatus
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.ExperimentalCoroutinesApi

class NewTripFragment : AbstractTripDetailsFragment() {

    lateinit var binding: FragmentNewTripBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewTripBinding.inflate(inflater, container, false)

        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.trip = localTrip.trip

        //TODO check if there is truck and pre populate

        truck?.let {
            //load truck odometer
        }

        binding.register.setOnClickListener {

            localTrip.trip?.apply {
                truckReg = truck!!.truckReg
                firstTrailerReg = truck!!.firstTrailerReg
                secondTrailerReg = truck?.secondTrailerReg
            }

            localTrip.trip?.tripStatus = TripStatus.WEIGH_EMPTY
            tripModel.createNewTrip(driver, localTrip).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Results.Loading -> showProgressBar("Creating new trip")
                    is Results.Success<*> -> {
                        endProgressBar()
                        showToast("Success!")
                        navController.popBackStack(R.id.homeFragment, false)
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