package com.example.shopapp.ui.home

import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.shopapp.R
import com.example.shopapp.adapters.BannerViewPagerAdapter
import com.example.shopapp.adapters.CategoryAdapter
import com.example.shopapp.adapters.ProductAdapter
import com.example.shopapp.databinding.FragmentHomeBinding
import com.example.shopapp.models.Category
import com.example.shopapp.ui.auth.AuthViewModel
import com.example.shopapp.utils.Constants.categories
import com.example.shopapp.utils.Constants.spinnerImages
import com.example.shopapp.utils.EventObserver
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var cateList: ArrayList<Category>
    private lateinit var categoryRv: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productRv: RecyclerView
    private lateinit var bannerAdapter: BannerViewPagerAdapter
    private lateinit var bannerRV: RecyclerView
    private val mainViewModel: MainViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var toggle: ActionBarDrawerToggle
    var category: String? = null
    private lateinit var spinner: Spinner
    private lateinit var geocoder: Geocoder

    private val binding get() = _binding
    private val imageList by lazy {
        ArrayList<String>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.getCategories()
        mainViewModel.getBanners()
        categoryData()
        productData()
        geocoder = Geocoder(requireContext(), Locale.getDefault())

        authViewModel.getUserInfo.observe(requireActivity()) { userInfo ->
            val location = geocoder.getFromLocation(
                userInfo.latitude.toDouble(),
                userInfo.longitude.toDouble(),
                1
            )

            val userLocation = location!!.first().countryCode + location.first().subLocality
            binding!!.locationTv.text = userLocation

        }
        subscribeToObservers()
        bannerAdapter = BannerViewPagerAdapter()
        binding?.apply {
            toggle = ActionBarDrawerToggle(
                requireActivity(),
                drawerLayout,
                R.string.open,
                R.string.close
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            val navView = view.findViewById<NavigationView>(R.id.navDrawer)
            navView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.notification -> {
                        binding!!.drawerLayout.close()
                        findNavController().navigate(R.id.action_homeFragment_to_notificationsFragment)
                    }

                }
                true
            }
        }
        view.findViewById<ViewPager2>(R.id.viewPagerBannersSlider).adapter = bannerAdapter
        spinner = view.findViewById<Spinner>(R.id.filter_spinner)
        categoryAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("category", it)

            }
            findNavController().navigate(
                R.id.action_homeFragment_to_categoryProductsFragment,
                bundle
            )

        }

        setupProductFilterSpinner()
        getProductFilterSpinnerSelectedItem()
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenStarted {
            launch {
                mainViewModel.categoryStatus.collect(
                    EventObserver(
                        onLoading = {
                            // binding!!.spinKit.isVisible = true
                        },
                        onSuccess = { data ->
                            //binding!!.spinKit.isVisible = false

                            categoryAdapter.differ.submitList(data.data?.data)
                        },
                        onError = {
                            //  binding!!.spinKit.isVisible = false
                        }
                    )
                )

            }
            launch {
                mainViewModel.bannersStatus.collect(
                    EventObserver(
                        onLoading = {

                        },
                        onSuccess = {
                            for (img in it.data!!) {
                                img.image?.let { it1 -> imageList.add(it1) }
                            }
                            bannerAdapter.differ.submitList(it.data)

                        }, onError = {

                        }
                    )
                )
            }
            launch {
                mainViewModel.allProductsStatus.collect(
                    EventObserver(
                        onLoading = {},
                        onSuccess = {
                            productAdapter.differ.submitList(it.data?.data)
                            Log.d("productAdapter", it.data!!.data.toString())

                        },
                        onError = {}
                    )
                )
            }
            launch {
                mainViewModel.allCategoryProductsStatus.collect(
                    EventObserver(
                        onLoading = {},
                        onSuccess = {
                            productAdapter.differ.submitList(it.data?.data)
                        },
                        onError = {}
                    )
                )
            }

        }

    }


    private fun categoryData() {
        categoryRv = binding!!.root.findViewById(R.id.category_rv)
        cateList = ArrayList()
        categoryAdapter = CategoryAdapter(cateList)
        categoryRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        categoryRv.adapter = categoryAdapter
    }

    private fun productData() {
        productRv = binding!!.root.findViewById(R.id.product_rv)
        productAdapter = ProductAdapter(1)
        productRv.layoutManager =
                // LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)

        productRv.adapter = productAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getProductFilterSpinnerSelectedItem() {
        spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    category = categories[position]
                    if (categories[position] == "All Product")
                        mainViewModel.getAllProducts()
                    else
                        mainViewModel.getProductsByCategoryID(42)
                    //Toast.makeText(requireContext(), "dasd", Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    category = categories[parent?.adapter?.getItem(0) as Int]
                    mainViewModel.getAllProducts()
                }
            }
    }

    private fun setupProductFilterSpinner() {
        val productFilterSpinnerAdapter = object : ArrayAdapter<String>
            (
            requireContext(), R.layout.product_filter_item, R.id.product_filter_tv, categories

        ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val productFilterImage: ImageView = view.findViewById(R.id.product_filter_img)
                val image = spinnerImages[position]
                productFilterImage.setImageResource(image)
                return view
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val productFilterImage: ImageView = view.findViewById(R.id.product_filter_img)
                val image = spinnerImages[position]
                productFilterImage.setImageResource(image)
                return view
            }
        }
        spinner.adapter = productFilterSpinnerAdapter
    }
}