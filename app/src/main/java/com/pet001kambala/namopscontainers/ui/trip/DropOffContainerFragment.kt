package com.pet001kambala.namopscontainers.ui.trip

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pet001kambala.namopscontainers.R
import com.pet001kambala.namopscontainers.databinding.FragmentDropOffContainerBinding
import com.pet001kambala.namopscontainers.model.TripStatus
import com.pet001kambala.namopscontainers.utils.DateUtil
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.copyOf
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.ExperimentalCoroutinesApi


class DropOffContainerFragment : AbstractTripDetailsFragment() {

    private lateinit var binding: FragmentDropOffContainerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDropOffContainerBinding.inflate(inflater,container,false)

        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.trip = localTrip.trip

        binding.memNote.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE

        binding.register.setOnClickListener {

            localTrip.trip!!.dropOffDate = DateUtil.localDateToday()
            val localTripCopy = localTrip.copyOf()!!
            localTripCopy.trip!!.tripStatus = TripStatus.COMPLETED

            tripModel.updateTripDetails(driver, localTripCopy).observe(viewLifecycleOwner) { results ->
                when (results) {
                    is Results.Loading -> showProgressBar("Saving end of trip info...")
                    is Results.Success<*> -> {
                        endProgressBar()
                        showToast("Saved.")
                        navController.popBackStack()

                        /**
                         * Clear the trip
                         */
                        val results = tripModel.completeTrip()
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