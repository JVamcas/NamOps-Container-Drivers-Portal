package com.pet001kambala.namopscontainers.utils

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.autocomplete.RecyclerViewPresenter
import com.pet001kambala.namopscontainers.model.AbstractModel
import com.pet001kambala.namopscontainers.model.JobCard
import com.pet001kambala.namopscontainers.model.JobCardItem
import com.pet001kambala.namopscontainers.ui.AbstractAdapter

class RecyclerPresenter(val jobCard: JobCard, context: Context) :
    RecyclerViewPresenter<JobCardItem>(context) {

    private val adapter =
        AutoCompleteViewAdapter(object : AbstractAdapter.ModelViewClickListener<JobCardItem> {
            override fun onEditModel(model: JobCardItem, pos: Int) {

            }

            override fun onDeleteModel(modelPos: Int) {
            }

            override fun onModelClick(model: JobCardItem) {
            }

            override fun onModelIconClick(model: JobCardItem) {
            }
        })

    override fun onQuery(query: CharSequence?) {
        adapter.modelList = (jobCard.jobCardItemList
            ?.filter {
            it.containerNo!!.contains(query!!) }?: ArrayList()) as ArrayList<JobCardItem>
    }

    override fun instantiateAdapter(): RecyclerView.Adapter<*> {
        return adapter
    }
}