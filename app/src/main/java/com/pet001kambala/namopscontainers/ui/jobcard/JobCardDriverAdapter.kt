package com.pet001kambala.namopscontainers.ui.jobcard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pet001kambala.namopscontainers.databinding.DriverViewBinding
import com.pet001kambala.namopscontainers.databinding.JobCardLayoutBinding
import com.pet001kambala.namopscontainers.model.Trip
import com.pet001kambala.namopscontainers.ui.AbstractAdapter

class JobCardDriverAdapter(mListener: ModelViewClickListener<Trip>):
    AbstractAdapter<Trip, JobCardDriverAdapter.ViewHolder> (mListener = mListener){


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var trip: Trip
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DriverViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.trip = get(position)
        val binding = DataBindingUtil.getBinding<DriverViewBinding>(holder.itemView)
        binding!!.trip = holder.trip
    }
}