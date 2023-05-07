package com.example.shopapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.R
import com.example.shopapp.adapters.CategoryAdapter2
import com.example.shopapp.databinding.FragmentCategoryBinding
import com.example.shopapp.models.Category
import com.example.shopapp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter2
    private lateinit var cateList: ArrayList<Category>
    private lateinit var categoryRv: RecyclerView
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getCategories()
        subscribeToObservers()
        categoryData()
        categoryAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("category", it)
            }
            findNavController().navigate(
                R.id.action_categoryFragment_to_categoryProductsFragment,
                bundle
            )

        }
    }
    private fun subscribeToObservers() {
        lifecycleScope.launchWhenStarted {
            launch {
                mainViewModel.categoryStatus.collect(
                    EventObserver(
                        onLoading = {
                             binding!!.spinKit.isVisible = true
                        },
                        onSuccess = { data ->
                            binding!!.spinKit.isVisible = false
                            categoryAdapter.differ.submitList(data.data?.data)
                        },
                        onError = {
                              binding!!.spinKit.isVisible = false
                        }
                    )
                )
            }
        }
    }
    private fun categoryData() {
        categoryRv = binding!!.categoryFragmentRv
        cateList = ArrayList()
        categoryAdapter = CategoryAdapter2(cateList)
        categoryRv.layoutManager =
            GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        categoryRv.adapter = categoryAdapter
    }
}