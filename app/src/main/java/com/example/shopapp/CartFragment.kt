package com.example.shopapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.shopapp.databinding.FragmentCartBinding
import com.example.shopapp.ui.home.MainViewModel
import com.example.shopapp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {


    private val mainViewModel: MainViewModel by viewModels()
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()

    }
    private fun subscribeToObservers() {
        lifecycleScope.launchWhenStarted {
            launch {
                mainViewModel.cartStatus.collect(
                    EventObserver(
                        onLoading = {
                            //   binding!!.spinKit.isVisible = true
                        },
                        onSuccess = {
                            //  binding!!.spinKit.isVisible = false
                        },
                        onError = {
                            // binding!!.spinKit.isVisible = false
                        }
                    )
                )

            }


        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}