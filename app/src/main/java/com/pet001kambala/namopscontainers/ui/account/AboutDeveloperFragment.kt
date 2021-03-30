package com.pet001kambala.namopscontainers.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pet001kambala.namopscontainers.databinding.FragmentAboutDeveloperBinding
import com.pet001kambala.namopscontainers.model.Driver
import com.pet001kambala.namopscontainers.ui.AbstractFragment

import kotlinx.coroutines.ExperimentalCoroutinesApi

class AboutDeveloperFragment : AbstractFragment() {

    private lateinit var binding: FragmentAboutDeveloperBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentAboutDeveloperBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.account = Driver(
            id = -1,
            firstName = "Petrus Mesias",
            lastName = "Kambala")
    }
}
