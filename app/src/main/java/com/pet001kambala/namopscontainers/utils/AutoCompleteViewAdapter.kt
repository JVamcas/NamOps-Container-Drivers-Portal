package com.pet001kambala.namopscontainers.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pet001kambala.namopscontainers.databinding.AutoCompleteLayoutBinding
import com.pet001kambala.namopscontainers.model.JobCardItem
import com.pet001kambala.namopscontainers.ui.AbstractAdapter

class AutoCompleteViewAdapter(mListener: ModelViewClickListener<JobCardItem>) :
    AbstractAdapter<JobCardItem, AutoCompleteViewAdapter.ViewHolder>(mListener) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var jobCardItem: JobCardItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            AutoCompleteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.jobCardItem = get(position)
        val binding = DataBindingUtil.getBinding<AutoCompleteLayoutBinding>(holder.itemView)
        binding!!.text = holder.jobCardItem.containerNo
    }
}