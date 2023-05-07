package com.example.shopapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.adapters.ProductAdapter
import com.example.shopapp.adapters.SearchAdapter
import com.example.shopapp.databinding.FragmentSearchBinding
import com.example.shopapp.ui.home.MainViewModel
import com.example.shopapp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchRv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setupRecyclerView()

        binding!!.searchTv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    mainViewModel.searchForProducts(query)
                    binding!!.searchRv.scrollToPosition(0)
                    binding!!.searchTv.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    mainViewModel.searchForProducts(newText)
                }
                return false
            }
        })

    }
    private fun subscribeToObservers() {
        lifecycleScope.launchWhenStarted {
            launch {
                mainViewModel.searchProductsStatus.collect(
                    EventObserver(
                        onLoading = {
                            binding!!.spinKit.isVisible = true
                        },
                        onSuccess = { searchResult ->
                            binding!!.spinKit.isVisible = false
                            binding!!.searchRv.visibility = View.VISIBLE
                            searchAdapter.products= searchResult.data?.data!!

                        },
                        onError = {
                            binding!!.searchRv.visibility = View.GONE
                            binding!!.spinKit.isVisible = false
                        }
                    )
                )

            }

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding!!.searchTv.setQuery("", false)
        binding!!.searchRv.visibility = View.GONE
        _binding = null
    }

    private fun setupRecyclerView() {
        searchRv = binding!!.searchRv
        searchAdapter = SearchAdapter()
        searchRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        searchRv.adapter = searchAdapter
    }

}