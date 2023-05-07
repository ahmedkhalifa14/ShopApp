package com.example.shopapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.shopapp.R
import com.example.shopapp.databinding.FragmentAccountBinding
import com.example.shopapp.models.Profile
import com.example.shopapp.ui.auth.AuthViewModel
import com.example.shopapp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AccountFragment : Fragment() {


    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.getUserProfileInfo()
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenStarted {
            launch {
                authViewModel.profileStatus.collect(
                    EventObserver(
                        onLoading = {
                            binding!!.spinKit.isVisible = true
                        },
                        onSuccess = {
                            binding!!.spinKit.isVisible = false

                            it.data?.let { it1 -> displayProfileData(it1) }
                        },
                        onError = {
                            binding!!.spinKit.isVisible = false
                        }
                    )
                )

            }


        }

    }

    private fun displayProfileData(profile: Profile) {
        binding!!.editProfileButton.isVisible =true
        binding!!.userEmail.text = profile.email
        binding!!.userName.text = profile.name
        binding!!.userPoints.text = profile.points.toString()
        Glide.with(requireActivity()).load(profile.image).placeholder(R.drawable.avatar).into(binding!!.profileImage)
        Glide.with(requireActivity()).load(profile.image).placeholder(R.drawable.profile).into(binding!!.profileBackgroundImage)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}