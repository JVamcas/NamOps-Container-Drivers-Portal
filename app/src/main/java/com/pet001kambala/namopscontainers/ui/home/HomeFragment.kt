package com.pet001kambala.namopscontainers.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
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
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.containerPickedUp
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.copyOf
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.lang.Exception

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

        tripModel.currentLocalTrip.observe(viewLifecycleOwner) {
            binding.trip = it?.trip

            it?.let { localTrip ->

                val tempTrip = localTrip.trip

                binding.tripLayout.dropOffBtn.isEnabled =
                    tempTrip?.scanContainer == false || tempTrip?.containerScanDate != null

                binding.tripLayout.scanContainer.setOnClickListener {

                    showWarningDialog(warningTxt = "Have you thermally scanned this container?",
                        object : WarningDialogListener {
                            override fun onOkWarning() {
                                val localCopy = localTrip.copyOf()
                                tripModel.viewModelScope.launch {
                                    localCopy?.trip?.containerScanDate = DateUtil.localDateToday()
                                    tripModel.updateTripDetails(
                                        driver = driver!!,
                                        localTrip = localCopy!!
                                    ).observe(viewLifecycleOwner) { results ->
                                        when (results) {
                                            is Results.Loading -> showProgressBar("Just a moment")
                                            is Results.Success<*> -> {
                                                endProgressBar()
                                                showToast("Saved.")

                                                binding.tripLayout.dropOffBtn.isEnabled = true

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
                binding.tripLayout.scanContainer.isVisible =
                    localTrip.trip?.containerScanDate == null
                            && localTrip.trip?.scanContainer == true
                            && localTrip.trip.containerPickedUp()

            }
        }

        with(binding.tripLayout) {
            weighEmptyBtn.setOnClickListener { navController.navigate(R.id.action_homeFragment_to_weighEmptyTruckFragment) }
            pickupBtn.setOnClickListener { navController.navigate(R.id.action_homeFragment_to_pickUpContainerFragment) }

            weighFullBisonBtn.setOnClickListener { navController.navigate(R.id.action_homeFragment_to_weighFullContainerFragment) }
            weighFullBridgeBtn.setOnClickListener { navController.navigate(R.id.action_homeFragment_to_weighFullContainerFragment) }
            dropOffBtn.setOnClickListener { navController.navigate(R.id.action_homeFragment_to_dropOffContainerFragment) }
        }

        val networkCallback: ConnectivityManager.NetworkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                //on network restored,
                /** 1. load cached trip and send to server **/

                tripModel.viewModelScope.launch {
                    try {
                        val currentTrip =
                            tripModel.tripDao.loadCurrentTrip()
                        if (currentTrip?.awaitingNetwork == true) {
                            println("Network state changed")
                            val tripComplete = currentTrip.trip?.tripStatus == TripStatus.COMPLETED
                            val liveData =
                                if (tripComplete)
                                    tripModel.completeTrip(
                                        driver = driver!!,
                                        localTrip = currentTrip
                                    )
                                else
                                    tripModel.updateTripDetails(
                                        driver = driver!!,
                                        localTrip = currentTrip
                                    )

                            liveData.observe(viewLifecycleOwner) { syncResults ->
                                when (syncResults) {
                                    is Results.Loading -> showProgressBar("Server sync...")
                                    is Results.Success<*> -> {
                                        endProgressBar()
                                        showToast("Sync completed.")
                                        liveData.removeObservers(viewLifecycleOwner)
                                    }
                                    else -> {
                                        endProgressBar()
                                        parseRepoResults(syncResults)
                                        liveData.removeObservers(viewLifecycleOwner)
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onLost(network: Network) {
                // network unavailable
            }
        }

        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
            connectivityManager.registerNetworkCallback(request, networkCallback)
        }
    }

    override fun onBackClick() {
        requireActivity().finish()
    }
}