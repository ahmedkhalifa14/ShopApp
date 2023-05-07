package com.example.shopapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.shopapp.R
import com.example.shopapp.adapters.ProductAdapter
import com.example.shopapp.databinding.FragmentCategoryProductsBinding
import com.example.shopapp.models.Product
import com.example.shopapp.utils.EventObserver
import com.example.shopapp.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryProductsFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var productsList: ArrayList<Product>
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productRecyclerView: RecyclerView
    private val args: CategoryProductsFragmentArgs by navArgs()
    private var _binding: FragmentCategoryProductsBinding? = null
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryProductsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category = args.category
        binding!!.headerTitle.text = args.category.name
        binding!!.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_categoryProductsFragment_to_homeFragment)
        }
        categoryData()
        subscribeToObservers()
        productAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("product", it)
            }
            findNavController().navigate(
                R.id.action_categoryProductsFragment_to_productDetailsFragment,
                bundle
            )

        }

        productAdapter.setOnAddToCartItemClickListener { product ->

            mainViewModel.addItemToCart(product.id!!)
        }
        productAdapter.setOnAddToFavItemClickListener { product ->
            mainViewModel.addItemToFavourites(product.id!!)
        }
    }

    private fun subscribeToObservers() {
        /*
        lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
        mainViewModel.allCategoryProductsStatus.collect(
        EventObserver(
        onLoading = {
        binding!!.spinKit.isVisible = true
        },
        onSuccess = { data ->
        binding!!.spinKit.isVisible = false
        productAdapter.differ.submitList(data.data?.data)
        },
        onError = {
        binding!!.spinKit.isVisible = false
        }
        )
        )
        }
        repeatOnLifecycle(Lifecycle.State.INITIALIZED) {
        mainViewModel.addItemToCartStatus.collect(
        EventObserver(
        onLoading = {

        },
        onSuccess = {
        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        },
        onError = {
        Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        }
        )
        )
        }
        }
         */
        lifecycleScope.launchWhenStarted {
            launch {
                mainViewModel.allCategoryProductsStatus.collect(
                    EventObserver(
                        onLoading = {
                            binding!!.spinKit.isVisible = true
                        },
                        onSuccess = { data ->
                            binding!!.spinKit.isVisible = false
                            productAdapter.differ.submitList(data.data?.data)
                        },
                        onError = {
                            binding!!.spinKit.isVisible = false
                        }
                    )
                )

            }
            launch {
                mainViewModel.addItemToCartStatus.collect(
                    EventObserver(
                        onLoading = {
                        },
                        onSuccess = { data ->

                            args.category.id?.let {
                                mainViewModel.getProductsByCategoryID(
                                    it,
                                    false
                                )
                            }
                            Toast.makeText(requireContext(), data.message, Toast.LENGTH_SHORT)
                                .show()
                        },
                        onError = {
                            snackbar(it)
                        }
                    )
                )

            }
            launch {
                mainViewModel.addItemToFavouritesStatus.collect(
                    EventObserver(
                        onLoading = {
                        },
                        onSuccess = {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                            args.category.id?.let { it1 ->
                                mainViewModel.getProductsByCategoryID(
                                    it1,
                                    false
                                )
                            }
                        },
                        onError = {
                            snackbar(it)
                        }
                    )

                )
            }


        }

    }

    private fun categoryData() {
        productRecyclerView = binding!!.categoryRV
        productsList = ArrayList()
        productAdapter = ProductAdapter(2)
        productRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        // LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        productRecyclerView.adapter = productAdapter
    }

    override fun onResume() {
        super.onResume()
        args.category.id?.let { mainViewModel.getProductsByCategoryID(it, true) }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}