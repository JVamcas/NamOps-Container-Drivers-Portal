package com.pet001kambala.namopscontainers.ui.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pet001kambala.namopscontainers.databinding.FragmentNewTripBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

class NewTripFragment : AbstractTripDetailsFragment() {

    lateinit var binding : FragmentNewTripBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewTripBinding.inflate(inflater,container,false)

        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.trip = trip


    }
}