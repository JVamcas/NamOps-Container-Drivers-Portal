package com.pet001kambala.namopscontainers.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import com.pet001kambala.namopscontainers.R
import com.pet001kambala.namopscontainers.databinding.FragmentLoginBinding
import com.pet001kambala.namopscontainers.model.Auth
import com.pet001kambala.namopscontainers.repo.AccountRepo
import com.pet001kambala.namopscontainers.ui.AbstractFragment
import com.pet001kambala.namopscontainers.utils.Results
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

open class LoginFragment : AbstractFragment() {

    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentLoginBinding.inflate(inflater,container,false)
        binding.auth = Auth()
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountModel.login().observeOnce(viewLifecycleOwner){results->
            when(results){
                is Results.Loading -> showProgressBar("Logging In...")
                is Results.Success<*> ->{
                    endProgressBar()
                    if(!results.dataIfNotSeen.isNullOrEmpty()){
                        showToast("Logged In.")
                        navController.popBackStack(R.id.homeFragment,false)
                    }
                }
                else -> {
                    endProgressBar()
                    parseRepoResults(results)
                }
            }
        }

        binding.loginBtn.setOnClickListener {
            accountModel.login(binding.auth!!).observe(viewLifecycleOwner){results->
                when(results){
                    is Results.Loading -> showProgressBar("Logging In...")
                    is Results.Success<*> ->{
                        endProgressBar()
                        if(!results.data.isNullOrEmpty()){
                            showToast("Logged In.")
                            navController.popBackStack(R.id.homeFragment,false)
                        }
                    }
                    else -> {
                        endProgressBar()
                        parseRepoResults(results)
                    }
                }
            }
        }
    }
}