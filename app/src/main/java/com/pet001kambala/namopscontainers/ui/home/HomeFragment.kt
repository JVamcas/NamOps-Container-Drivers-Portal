package com.pet001kambala.namopscontainers.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.viewModelScope
import com.pet001kambala.namopscontainers.R
import com.pet001kambala.namopscontainers.databinding.FragmentHomeBinding
import com.pet001kambala.namopscontainers.model.TripStatus
import com.pet001kambala.namopscontainers.ui.AbstractFragment
import com.pet001kambala.namopscontainers.utils.Const
import com.pet001kambala.namopscontainers.utils.DateUtil
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.copyOf
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class HomeFragment : AbstractFragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.preAssignedJob.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Const.TOOLBAR_TITLE, "Pre-Assigned Jobs")
            bundle.putBoolean(Const.IS_PRE_ASSIGNED, true)
            navController.navigate(R.id.action_nav_home_to_jobCardListFragment, bundle)
        }

        binding.unassignedJob.setOnClickListener {
            val bundle = Bundle().also { it.putString(Const.TOOLBAR_TITLE, "Un-Assigned Jobs") }
            navController.navigate(R.id.action_nav_home_to_jobCardListFragment, bundle)

        }

        tripModel.currentLocalTrip.observe(viewLifecycleOwner) { localTrip ->
            binding.trip = localTrip?.trip

            binding.tripLayout.scanContainer.setOnClickListener {
                showWarningDialog(warningTxt = "Have you thermally scanned this container?",
                    object : WarningDialogListener {
                        override fun onOkWarning() {
                            val localCopy = localTrip.copyOf()
                            tripModel.viewModelScope.launch {
                                localTrip.trip?.containerScanDate = DateUtil.localDateToday()
                                tripModel.updateTripDetails(
                                    driver = driver,
                                    localTrip = localCopy!!
                                ).observe(viewLifecycleOwner) { results ->
                                    when (results) {
                                        is Results.Loading -> showProgressBar("Just a moment")
                                        is Results.Success<*> -> {
                                            endProgressBar()
                                            showToast("Saved.")
                                        }
                                        else -> {
                                            endProgressBar()
                                            parseRepoResults(results)
                                        }
                                    }
                                }
                            }
                        }

                        override fun onCancelWarning() {

                        }
                    })
            }
        }

        with(binding.tripLayout) {
            weighEmptyBtn.setOnClickListener { navController.navigate(R.id.action_homeFragment_to_weighEmptyTruckFragment) }
            pickupBtn.setOnClickListener { navController.navigate(R.id.action_homeFragment_to_pickUpContainerFragment) }

            weighFullBisonBtn.setOnClickListener { navController.navigate(R.id.action_homeFragment_to_weighFullContainerFragment) }
            weighFullBridgeBtn.setOnClickListener { navController.navigate(R.id.action_homeFragment_to_weighFullContainerFragment) }
            dropOffBtn.setOnClickListener { navController.navigate(R.id.action_homeFragment_to_dropOffContainerFragment) }

            scanContainer.isVisible = trip?.containerScanDate == null && trip?.scanContainer == true

        }
    }

    override fun onBackClick() {
        requireActivity().finish()
    }
}