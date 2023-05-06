package com.example.shopapp.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.shopapp.R
import com.example.shopapp.adapters.OnBoardingViewPagerAdapter
import com.example.shopapp.animations.ZoomOutPageTransformer
import com.example.shopapp.databinding.FragmentOnBoardingBinding
import com.example.shopapp.ui.auth.AuthViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding
    private val authViewModel:AuthViewModel by viewModels()

    val navOptions =
        NavOptions.Builder()
            .setPopUpTo(R.id.onBoardingFragment, true)
            .build()
    companion object {
        const val MAX_STEP = 4
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.viewPager2.adapter = OnBoardingViewPagerAdapter()
        TabLayoutMediator(binding!!.tabLayout, binding!!.viewPager2) { tab, position -> }.attach()
        binding!!.viewPager2.setPageTransformer(ZoomOutPageTransformer())
        binding!!.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == MAX_STEP - 1) {
                    binding!!.btnNext.text = getString(R.string.intro_get_started)
                    binding!!.btnNext.contentDescription =
                        getString(R.string.intro_get_started)
                    binding!!.btnNext.setOnClickListener {
                        authViewModel.setFirstTimeLaunch(true)
                       findNavController().navigate(R.id.registerFragment, null, navOptions)

                    }
                } else {
                    binding!!.btnNext.text = getString(R.string.next)
                    binding!!.btnNext.contentDescription = getString(R.string.next)
                }
            }
        })
        binding!!.btnSkip.setOnClickListener {
            authViewModel.setFirstTimeLaunch(true)
            findNavController().navigate(R.id.registerFragment, null, navOptions)


        }
        binding!!.btnNext.setOnClickListener {
            if (binding!!.btnNext.text.toString() == getString(R.string.intro_get_started)) {
                authViewModel.setFirstTimeLaunch(true)
                findNavController().navigate(R.id.registerFragment)

            } else {
                val current = (binding!!.viewPager2.currentItem) + 1
                binding!!.viewPager2.currentItem = current
                if (current >= MAX_STEP - 1) {
                    binding!!.btnNext.text = getString(R.string.intro_get_started)
                    binding!!.btnNext.contentDescription =
                        getString(R.string.intro_get_started)
                    binding!!.btnNext.setOnClickListener {
                        authViewModel.setFirstTimeLaunch(true)
                        findNavController().navigate(R.id.registerFragment, null, navOptions)

                    }
                } else {
                    binding!!.btnNext.text = getString(R.string.next)
                    binding!!.btnNext.contentDescription = getString(R.string.next)
                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding =null
    }

}
