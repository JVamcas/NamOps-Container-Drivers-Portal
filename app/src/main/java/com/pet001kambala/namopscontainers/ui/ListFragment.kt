package com.pet001kambala.namopscontainers.ui


import androidx.recyclerview.widget.RecyclerView
import com.pet001kambala.namopscontainers.model.AbstractModel

interface ListFragment<K : AbstractModel, T : RecyclerView.ViewHolder> :
    AbstractAdapter.ModelViewClickListener<K> {

    fun handleRecycleView(recyclerView: RecyclerView, mListener: AbstractAdapter.ModelViewClickListener<K>)
}