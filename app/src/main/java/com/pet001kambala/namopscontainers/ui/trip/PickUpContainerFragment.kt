package com.pet001kambala.namopscontainers.ui.trip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.viewModelScope
import com.otaliastudios.autocomplete.Autocomplete
import com.pet001kambala.namopscontainers.R
import com.pet001kambala.namopscontainers.databinding.FragmentNewTripBinding
import com.pet001kambala.namopscontainers.databinding.FragmentPickUpContainerBinding
import com.pet001kambala.namopscontainers.model.JobCardItem
import com.pet001kambala.namopscontainers.model.TripStatus
import com.pet001kambala.namopscontainers.model.Truck
import com.pet001kambala.namopscontainers.utils.*
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.copyOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

class PickUpContainerFragment : AbstractTripDetailsFragment() {

    lateinit var binding: FragmentPickUpContainerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPickUpContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @InternalCoroutinesApi
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tripModel.currentLocalTrip.observe(viewLifecycleOwner) {
            it?.let { localTrip ->

                tripModel.viewModelScope.launch {

                    binding.trip = localTrip.trip

                    truck?.let { truck ->
                        tripModel.loadTruckODO(truck = truck).observe(requireActivity()) {
                            if (it is Results.Success<*>) {
                                if (!it.data.isNullOrEmpty())
                                    localTrip.trip!!.pickUpODM =
                                        (it.data as ArrayList<Truck>).first().odoMeter

                            } else localTrip.trip!!.pickUpODM = "0.0"
                        }
                    }

                    val jobCard = tripModel.tripDao.loadCurrentJobCard()
                    binding.jobcard = jobCard

                    arrayListOf(
                        binding.container1,
                        binding.container2,
                        binding.container3
                    ).forEach {
                        it.setAdapter(
                            ArrayAdapter(
                                binding.root.context,
                                R.layout.auto_select_layout,
                                jobCard?.jobCardItemList?.filterNot { it.wasPickedUp }
                                    ?.map { it.containerNo }?.toList()!!
                            )
                        )
                        it.threshold = 1
                    }

                    binding.register.setOnClickListener {

                        localTrip.trip!!.actualPickUpDate = DateUtil.localDateToday()
                        val localTripCopy = localTrip.copyOf()!!

                        localTripCopy.trip!!.tripStatus =
                            if (localTrip.trip?.useBison == true) TripStatus.BISON else TripStatus.WEIGH_FULL

                        val jobCardCopy = jobCard.copyOf()
                        jobCardCopy?.jobCardItemList =
                            jobCardCopy?.filterPickedUpContainers(trip = localTripCopy.trip!!)

                        localTripCopy.trip?.pickUpLocationGPS = location
                        tripModel.updateTripDetails(
                            driver = driver!!,
                            localTrip = localTripCopy
                        ).observe(viewLifecycleOwner) { results ->
                            when (results) {
                                is Results.Loading -> showProgressBar("Saving...")
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
        }
    }
}