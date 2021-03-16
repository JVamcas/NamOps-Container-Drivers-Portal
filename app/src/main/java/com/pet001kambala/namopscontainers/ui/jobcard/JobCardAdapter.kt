package com.pet001kambala.namopscontainers.ui.jobcard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pet001kambala.namopscontainers.databinding.JobCardLayoutBinding
import com.pet001kambala.namopscontainers.model.JobCard
import com.pet001kambala.namopscontainers.ui.AbstractAdapter

class JobCardAdapter(mListener: ModelViewClickListener<JobCard>) :
    AbstractAdapter<JobCard, JobCardAdapter.ViewHolder>(mListener) {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var jobCard: JobCard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            JobCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding.root
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.jobCard = get(position)
        val binding = DataBindingUtil.getBinding<JobCardLayoutBinding>(holder.itemView)
        binding!!.jobCard = holder.jobCard
        binding.root.setOnClickListener {
            mListener.onModelClick(holder.jobCard)
        }
    }
}