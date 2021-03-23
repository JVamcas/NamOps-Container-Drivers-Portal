package com.pet001kambala.namopscontainers.ui.trip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.lifecycle.viewModelScope
import com.otaliastudios.autocomplete.Autocomplete
import com.pet001kambala.namopscontainers.R
import com.pet001kambala.namopscontainers.databinding.FragmentNewTripBinding
import com.pet001kambala.namopscontainers.databinding.FragmentPickUpContainerBinding
import com.pet001kambala.namopscontainers.model.JobCardItem
import com.pet001kambala.namopscontainers.model.TripStatus
import com.pet001kambala.namopscontainers.utils.*
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.copyOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.trip = localTrip.trip
        tripModel.viewModelScope.launch {
            val jobcard = tripModel.tripDao.loadCurrentJobCard()

            Autocomplete.on<JobCardItem>(binding.container1)
                .with(SimpleAutoCompletePolicy())
//            .with(autocompleteCallback)
                .with(RecyclerPresenter(context = requireContext(), jobCard = jobcard!!))
                .build();

            binding.register.setOnClickListener {

                localTrip.trip!!.actualPickUpDate = DateUtil.localDateToday()
                val localTripCopy = localTrip.copyOf()!!
                localTripCopy.trip!!.tripStatus =
                    if (localTrip.trip?.useBison == true) TripStatus.BISON else TripStatus.WEIGH_FULL

                tripModel.updateTripDetails(driver, localTripCopy)
                    .observe(viewLifecycleOwner) { results ->
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