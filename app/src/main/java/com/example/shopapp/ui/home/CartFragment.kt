package com.example.shopapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.adapters.CartAdapter
import com.example.shopapp.databinding.FragmentCartBinding
import com.example.shopapp.utils.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {


    private val mainViewModel: MainViewModel by viewModels()
    private var _binding: FragmentCartBinding? = null

    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartRecyclerView: RecyclerView
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
        setupRecyclerView()
        subscribeToObservers()
        mainViewModel.getAllCartItems(true)
        cartAdapter.setOnItemCounterClickListener { cartItem, i ->
            mainViewModel.updateCartItemQuantity(cartItem.id, cartItem.quantity)
        }
        cartAdapter.setOnItemDeleteClickListener { cartItem ->
            mainViewModel.deleteItemFromCarts(cartItem.id)
        }
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenStarted {
            launch {
                mainViewModel.cartStatus.collect(
                    EventObserver(
                        onLoading = {
                            binding!!.spinKit.isVisible = true
                            binding!!.cardView.isVisible = false
                            binding!!.bottomBar.isVisible = false
                        },
                        onSuccess = { cartItems ->
                            binding!!.spinKit.isVisible = false
                            Log.d("cart", cartItems.data!!.cart_items.toString())
                            if (cartItems.data?.cart_items!!.isEmpty()) {
                                binding!!.cardView.isVisible = true
                                binding!!.bottomBar.isVisible = false

                            }
                            cartAdapter.cartList = cartItems.data.cart_items.toMutableList()
                            binding!!.bottomBar.isVisible = true
                            binding!!.subtotalPrice.text =
                                "EGP" + cartItems.data.sub_total.toString()
                        },
                        onError = {
                            binding!!.spinKit.isVisible = false
                            binding!!.bottomBar.isVisible = false
                            Log.d("cart", it)
                        }
                    )
                )

            }
            launch {
                mainViewModel.updateCartItemQuantityStatus.collect(
                    EventObserver(
                        onLoading = {

                        },
                        onSuccess = {
                            mainViewModel.getAllCartItems(false)
                            binding!!.subtotalPrice.text = "EGP" + it.data!!.sub_total.toString()
                        },
                        onError = {

                        }
                    )
                )
            }
            launch {
                mainViewModel.deleteItemFromCartsStatus.collect(
                    EventObserver(
                        onLoading = {

                        },
                        onSuccess = {
                            mainViewModel.getAllCartItems(false)
                            binding!!.subtotalPrice.text = "EGP" + it.data!!.sub_total.toString()
                        },
                        onError = {

                        }
                    )
                )
            }


        }

    }

    private fun setupRecyclerView() {
        cartRecyclerView = binding!!.cartRV
        cartAdapter = CartAdapter()
        cartRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        cartRecyclerView.adapter = cartAdapter

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}