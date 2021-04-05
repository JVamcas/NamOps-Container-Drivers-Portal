package com.pet001kambala.namopscontainers.ui.jobcard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import com.pet001kambala.namopscontainers.MainActivity
import com.pet001kambala.namopscontainers.R
import com.pet001kambala.namopscontainers.databinding.FragmentDriversOnJobCardBinding
import com.pet001kambala.namopscontainers.model.Driver
import com.pet001kambala.namopscontainers.model.JobCard
import com.pet001kambala.namopscontainers.model.Trip
import com.pet001kambala.namopscontainers.ui.AbstractListFragment
import com.pet001kambala.namopscontainers.ui.trip.TripViewModel
import com.pet001kambala.namopscontainers.utils.Const
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.convert
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

class DriversOnJobCardFragment : AbstractListFragment<Trip, JobCardDriverAdapter.ViewHolder>() {
    lateinit var jobCard: JobCard
    private lateinit var binding: FragmentDriversOnJobCardBinding

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val json = it.getString(Const.JOB_CARD)
            json?.let { jobCard = json.convert() }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDriversOnJobCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).actionBar?.title =
            "Drivers - JobCard: ${jobCard.jobCardNo}"

        tripModel.viewModelScope.launch {
            binding.driversCount = 1
            tripModel.loadActiveTripsOnJobCard(driver = driver!!, jobCardNo = jobCard.jobCardNo)
                .observe(viewLifecycleOwner) { results ->
                    when (results) {
                        is Results.Loading -> showProgressBar("Just a moment...")
                        is Results.Success<*> -> {
                            endProgressBar()
                            val data = results.data as ArrayList<Trip>

                            binding.driversCount = if (!data.isNullOrEmpty()) {
                                mAdapter.modelList = data
                                data.size
                            } else 0
                        }
                        else -> {
                            endProgressBar()
                            parseRepoResults(results)
                        }
                    }
                }
        }
    }

    override fun initAdapter() {
        mAdapter = JobCardDriverAdapter(this)
    }

    override fun onEditModel(model: Trip, pos: Int) {
    }

    override fun onDeleteModel(modelPos: Int) {
    }

    override fun onModelClick(model: Trip) {
    }

    override fun onModelIconClick(model: Trip) {
    }
}