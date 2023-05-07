package com.example.shopapp.ui.navdrawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.shopapp.databinding.FragmentTermsAndConditionsBinding
import com.example.shopapp.ui.home.MainViewModel
import com.example.shopapp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TermsAndConditionsFragment : Fragment() {


    private var _binding: FragmentTermsAndConditionsBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTermsAndConditionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        mainViewModel.getSettings()
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenStarted {
            launch {
                mainViewModel.getSettingsStatus.collect(
                    EventObserver(
                        onLoading = {

                        },
                        onSuccess = { data ->
                            binding.aboutTv.text = data.data!!.about
                            binding.termTv.text = data.data.terms
                        },
                        onError = {
                        }
                    )
                )
            }
        }
    }
}