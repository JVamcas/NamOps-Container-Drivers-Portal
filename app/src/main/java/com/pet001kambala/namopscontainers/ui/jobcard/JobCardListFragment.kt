package com.pet001kambala.namopscontainers.ui.jobcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.pet001kambala.namopscontainers.MainActivity
import com.pet001kambala.namopscontainers.databinding.FragmentJobcardListBinding
import com.pet001kambala.namopscontainers.model.JobCard
import com.pet001kambala.namopscontainers.ui.AbstractListFragment
import com.pet001kambala.namopscontainers.utils.Const
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.ExperimentalCoroutinesApi


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

        jobCardModel.allJobCards.observe(viewLifecycleOwner) {

            when (it) {
                is Results.Loading -> showProgressBar("Just a moment...")
                is Results.Success<*> -> {
                    endProgressBar()

                    val preAssigned =
                        (it.data as ArrayList<JobCard>).filter { it.jobCardItemList.any { it.driverId != null } }
                    val unAssigned =
                        it.data.filter { it.jobCardItemList.any { it.driverId == null } }
                    val data =
                        (if (isPreAssignedJobs) preAssigned else unAssigned) as ArrayList<JobCard>

                    binding.jobCardCount = data.size

                    if (!data.isNullOrEmpty()) {
                        mAdapter.modelList = data
                        binding.jobCardCount = mAdapter.itemCount
                    }
                }
                else -> {
                    endProgressBar()
                    parseRepoResults(it)
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

    }

    override fun onModelIconClick(model: JobCard) {

    }
}