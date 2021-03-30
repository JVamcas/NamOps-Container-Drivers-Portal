package com.pet001kambala.namopscontainers.ui.jobcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import com.pet001kambala.namopscontainers.MainActivity
import com.pet001kambala.namopscontainers.R
import com.pet001kambala.namopscontainers.databinding.FragmentJobcardListBinding
import com.pet001kambala.namopscontainers.model.Driver
import com.pet001kambala.namopscontainers.model.JobCard
import com.pet001kambala.namopscontainers.repo.JobCardRepo
import com.pet001kambala.namopscontainers.ui.AbstractFragment
import com.pet001kambala.namopscontainers.ui.AbstractListFragment
import com.pet001kambala.namopscontainers.ui.trip.TripViewModel
import com.pet001kambala.namopscontainers.utils.Const
import com.pet001kambala.namopscontainers.utils.ParseUtil.Companion.toJson
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.lang.Exception


class JobCardListFragment : AbstractListFragment<JobCard, JobCardAdapter.ViewHolder>() {

    lateinit var binding: FragmentJobcardListBinding
    lateinit var toolbarTitle: String
    private var isPreAssignedJobs = false
    private val jobCardModel: JobCardViewModel by activityViewModels()

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            toolbarTitle = it.getString(Const.TOOLBAR_TITLE)!!
            isPreAssignedJobs = it.getBoolean(Const.IS_PRE_ASSIGNED)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobcardListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar?.title = toolbarTitle

        handleRecycleView(binding.accountsRecycler, this)

        driver?.let { tempDriver ->

            jobCardModel.viewModelScope.launch {
                binding.jobCardCount = 1
                showProgressBar("Just a moment...")

                if (isPreAssignedJobs) {
                    val results = JobCardRepo().loadPreAssignedJobCards(tempDriver)
                    endProgressBar()
                    if (results is Results.Success<*>) {
                        val data = results.data as ArrayList<JobCard>

                        binding.jobCardCount = data.size

                        if (!data.isNullOrEmpty()) {
                            mAdapter.modelList = data
                            binding.jobCardCount = data.size
                        }
                    } else
                        parseRepoResults(results)

                } else {

                    val results = JobCardRepo().loadUnAssignedJobCards(tempDriver)
                    endProgressBar()
                    if (results is Results.Success<*>) {
                        val data = results.data as ArrayList<JobCard>

                        binding.jobCardCount = data.size

                        if (!data.isNullOrEmpty()) {
                            mAdapter.modelList = data
                            binding.jobCardCount = data.size
                        }
                    } else
                        parseRepoResults(results)
                }
            }
        }
    }

    override fun initAdapter() {
        mAdapter = JobCardAdapter(this)
    }

    override fun onEditModel(model: JobCard, pos: Int) {

    }

    override fun onDeleteModel(modelPos: Int) {

    }

    override fun onModelClick(model: JobCard) {
        showWarningDialog(
            warningTxt = "Would you like to take this JobCard?",
            object : WarningDialogListener {
                override fun onOkWarning() {
                    tripModel.viewModelScope.launch {
                        try {
                            tripModel.tripDao.clearJobCardTable()
                            tripModel.tripDao.insertJobCard(model)
                        } catch (e: Exception) {
                        }


                        val bundle = Bundle().also { it.putString(Const.JOB_CARD, model.toJson()) }
                        navController.navigate(
                            R.id.action_taskListFragment_to_newTripFragment,
                            bundle
                        )
                    }
                }

                override fun onCancelWarning() {

                }
            })

    }

    override fun onModelIconClick(model: JobCard) {

    }
}