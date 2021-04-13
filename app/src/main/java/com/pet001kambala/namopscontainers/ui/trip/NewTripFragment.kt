package com.pet001kambala.namopscontainers.ui.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import com.pet001kambala.namopscontainers.R
import com.pet001kambala.namopscontainers.databinding.FragmentNewTripBinding
import com.pet001kambala.namopscontainers.model.LocalTrip
import com.pet001kambala.namopscontainers.model.Trip
import com.pet001kambala.namopscontainers.model.TripStatus
import com.pet001kambala.namopscontainers.model.Truck
import com.pet001kambala.namopscontainers.repo.TripRepo
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.copyOf
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

class NewTripFragment : AbstractTripDetailsFragment() {

    lateinit var binding: FragmentNewTripBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewTripBinding.inflate(inflater, container, false)

        return binding.root
    }

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tripModel.viewModelScope.launch {

            val jobCard = tripModel.tripDao.loadCurrentJobCard()
            val jobCardItem = jobCard?.jobCardItemList!![0]
            val tempTrp = Trip(
                weighBridgeName = jobCardItem.weighBridgeName
            ).apply {
                this.driver = this@NewTripFragment.driver
                useBison = jobCardItem.useBison
                useWeighBridge = jobCardItem.useWeighBridge
                designatePickUpDate = jobCardItem.designatePickUpDate
                scanContainer = jobCardItem.scanContainer
                pickUpLocationName = jobCardItem.pickUpLocationName
            }
            val localTrip = LocalTrip().apply { trip = tempTrp }

            binding.trip = localTrip.trip

            truck?.let { truck ->
                tripModel.loadTruckODO(truck = truck).observe(viewLifecycleOwner) {
                    if (it is Results.Success<*>) {
                        if (!it.data.isNullOrEmpty())
                            localTrip.trip!!.startODM =
                                (it.data as ArrayList<Truck>).first().odoMeter

                    } else localTrip.trip!!.startODM = "0.0"
                }
            }

            binding.register.setOnClickListener {
                localTrip.trip?.apply {
                    truckReg = truck!!.truckReg
                    firstTrailerReg = truck!!.firstTrailerReg
                    secondTrailerReg = truck?.secondTrailerReg
                }

                localTrip.trip?.tripStatus =
                    if (localTrip.trip?.useBison == true) TripStatus.PICK_UP else TripStatus.WEIGH_EMPTY

                val localTripCopy = localTrip.copyOf().also {
                    it?.trip?.tripStatus =
                        if (it?.trip?.useBison != true) TripStatus.WEIGH_EMPTY else TripStatus.PICK_UP
                }

                localTripCopy?.trip?.startLocationGPS = location

                tripModel.createNewTrip(driver!!, localTripCopy!!)
                    .observe(viewLifecycleOwner) { result ->
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
}