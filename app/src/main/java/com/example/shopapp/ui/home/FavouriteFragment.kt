package com.example.shopapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.adapters.FavouriteItemsAdapter
import com.example.shopapp.databinding.FragmentFavouriteBinding
import com.example.shopapp.ui.home.MainViewModel
import com.example.shopapp.utils.EventObserver
import com.example.shopapp.utils.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavouriteFragment : Fragment() {


    private val mainViewModel: MainViewModel by viewModels()
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding
    private lateinit var favAdapter: FavouriteItemsAdapter
    private lateinit var favRecyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        subscribeToObservers()
        mainViewModel.getFavouriteItems(true)
        favAdapter.setOnFavItemClickListener {favItem ->
            mainViewModel.deleteItemFromFavourites(favItem.id)
        }

    }
    private fun subscribeToObservers() {
        lifecycleScope.launchWhenStarted {
            launch {
                mainViewModel.favouriteStatus.collect(
                    EventObserver(
                        onLoading = {
                            binding!!.spinKit.isVisible = true
                            binding!!.cardView.isVisible = false

                        },
                        onSuccess = {favourites ->
                            binding!!.spinKit.isVisible = false
                            if (favourites.data!!.data.isEmpty()){
                                binding!!.cardView.isVisible = true
                            }else
                                favAdapter.favourites=favourites.data.data
                        },
                        onError = {
                            binding!!.spinKit.isVisible = false
                            binding!!.noItemsTv.text = it.toString()
                            binding!!.cardView.isVisible = true                        }
                    )
                )

            }
            launch {
                mainViewModel.deleteItemFromFavouritesStatus.collect(
                    EventObserver(
                        onLoading = {},
                        onSuccess = {
                            mainViewModel.getFavouriteItems(false)
                        },
                        onError = {
                            snackbar(it)
                        }
                    )
                )
            }


        }

    }
    private fun setupRecyclerView() {
        favRecyclerView = binding!!.favRV
        favAdapter = FavouriteItemsAdapter()
        favRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        favRecyclerView.adapter = favAdapter

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}