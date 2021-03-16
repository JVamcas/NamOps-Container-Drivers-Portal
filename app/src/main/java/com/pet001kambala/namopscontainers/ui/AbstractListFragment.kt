package com.pet001kambala.namopscontainers.ui

import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pet001kambala.namopscontainers.model.AbstractModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

abstract class AbstractListFragment<K: AbstractModel, T: RecyclerView.ViewHolder>(
    var leftSwipe: Boolean = false, var rightSwipe: Boolean = false
)
    : AbstractFragment(), ListFragment<K,T> {

    lateinit var mAdapter: AbstractAdapter<K, T>

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
    }

    abstract fun initAdapter()

    override fun handleRecycleView(recyclerView: RecyclerView, mListener: AbstractAdapter.ModelViewClickListener<K>) {

        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)

        val swipeCallback = object : ItemSwipeCallback<K, T>(mAdapter, mListener,leftSwipe,rightSwipe) {}
        val touchHelper = ItemTouchHelper(swipeCallback)
        touchHelper.attachToRecyclerView(recyclerView)
    }
}