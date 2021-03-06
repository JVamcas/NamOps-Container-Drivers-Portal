package com.pet001kambala.namopscontainers.ui.trip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pet001kambala.namopscontainers.R
import com.pet001kambala.namopscontainers.databinding.FragmentWeighFullContainerBinding
import com.pet001kambala.namopscontainers.model.TripStatus
import com.pet001kambala.namopscontainers.utils.DateUtil
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.copyOf
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.ExperimentalCoroutinesApi


class WeighFullContainerFragment : AbstractTripDetailsFragment() {

    private lateinit var binding: FragmentWeighFullContainerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentWeighFullContainerBinding.inflate(inflater, container, false)

        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tripModel.currentLocalTrip.observe(viewLifecycleOwner) {
            it?.let { localTrip ->

                binding.trip = localTrip.trip


                binding.register.setOnClickListener {

                    localTrip.trip!!.dateWeightBridgeFull = DateUtil.localDateToday()
                    val localTripCopy = localTrip.copyOf()!!
                    localTripCopy.trip!!.tripStatus = TripStatus.DROP_OFF

                    tripModel.updateTripDetails(driver!!, localTripCopy)
                        .observe(viewLifecycleOwner) { results ->
                            when (results) {
                                is Results.Loading -> showProgressBar("Saving truck weight...")
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