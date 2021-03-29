package com.pet001kambala.namopscontainers.ui.trip

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import com.pet001kambala.namopscontainers.R
import com.pet001kambala.namopscontainers.databinding.FragmentDropOffContainerBinding
import com.pet001kambala.namopscontainers.model.TripStatus
import com.pet001kambala.namopscontainers.model.Truck
import com.pet001kambala.namopscontainers.utils.DateUtil
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.copyOf
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch


class DropOffContainerFragment : AbstractTripDetailsFragment() {

    private lateinit var binding: FragmentDropOffContainerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDropOffContainerBinding.inflate(inflater, container, false)

        return binding.root
    }

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tripModel.currentLocalTrip.observe(viewLifecycleOwner) {
            it?.let { localTrip ->


                binding.memNote.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE

                tripModel.viewModelScope.launch {

                    binding.trip = localTrip.trip

                    truck?.let {truck ->
                        tripModel.loadTruckODO(truck = truck).observe(viewLifecycleOwner) {
                            if (it is Results.Success<*>) {
                                if (!it.data.isNullOrEmpty())
                                    localTrip.trip!!.dropOffODM =
                                        (it.data as ArrayList<Truck>).first().odoMeter

                            }else localTrip.trip?.dropOffODM = "0.0"
                        }
                    }

                    val jobCard = tripModel.tripDao.loadCurrentJobCard()

                    binding.register.setOnClickListener {

                        localTrip.trip!!.dropOffDate = DateUtil.localDateToday()
                        val localTripCopy = localTrip.copyOf()!!
                        localTripCopy.trip!!.tripStatus = TripStatus.COMPLETED

                        val jobCardCopy = jobCard.copyOf()

                        jobCardCopy?.jobCardItemList = jobCardCopy?.filterPickedUpContainers(trip = localTrip.trip!!)

                        tripModel.completeTrip(
                            driver = driver,
                            localTrip = localTripCopy,
                            jobCard = jobCardCopy!!
                        )
                            .observe(viewLifecycleOwner) { results ->
                                when (results) {
                                    is Results.Loading -> showProgressBar("Saving end of trip info...")
                                    is Results.Success<*> -> {
                                        endProgressBar()
                                        showToast("Trip completed.")
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
        }
    }
}