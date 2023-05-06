package com.example.shopapp.ui.onboarding

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shopapp.R
import com.example.shopapp.data.local.DataStoreManager
import com.example.shopapp.databinding.FragmentSplashBinding
import com.example.shopapp.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding
    private var isFirstTimeLaunchVar: Boolean? = false
    private var isLogin: Boolean? = false
    private val authViewModel: AuthViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            authViewModel.getUserInfo.observe(requireActivity()) { userInfo ->
                if (!(userInfo.token.isNullOrEmpty()))
                    isLogin = true
            }
        }
        binding.apply {
            authViewModel.isFirstTimeLaunch.observe(requireActivity()) { isFirstTimeLaunch ->
                Timber.tag("token").d(isFirstTimeLaunch.toString())
                isFirstTimeLaunchVar = when (isFirstTimeLaunch) {
                    true -> {
                        true

                    }
                    false -> {
                        false
                    }
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            Handler(Looper.myLooper()!!).postDelayed(

                {
                    if (isFirstTimeLaunchVar == true && isLogin == true) {
                        lifecycleScope.launchWhenResumed {
                            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                        }


                    } else if (isFirstTimeLaunchVar == true && isLogin == false) {
                        lifecycleScope.launchWhenResumed {
                            findNavController().navigate(R.id.action_splashFragment_to_registerFragment)
                        }
                    } else {
                        lifecycleScope.launchWhenResumed {
                            findNavController().navigate(R.id.action_splashFragment_to_onBoardingFragment)
                        }
                    }

                }, 4000
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}