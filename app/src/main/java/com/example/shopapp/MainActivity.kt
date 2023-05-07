package com.example.shopapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.shopapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        navController = findNavController(R.id.navHostFragment)
        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment,
                R.id.onBoardingFragment,
                R.id.loginFragment,
                R.id.registerFragment,
                R.id.categoryProductsFragment,
                R.id.productDetailsFragment,
                R.id.cartFragment,
                R.id.termsAndConditionsFragment,
                R.id.searchFragment
                ->
                    binding.bottomNavigationView.isVisible = false

                else -> binding.bottomNavigationView.isVisible = true
            }
        }
    }
}
