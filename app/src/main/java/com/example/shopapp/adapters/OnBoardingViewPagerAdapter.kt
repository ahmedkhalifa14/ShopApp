package com.example.shopapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopapp.R
import com.example.shopapp.databinding.SplashScreenItemBinding
import com.example.shopapp.ui.onboarding.OnBoardingFragment.Companion.MAX_STEP

class OnBoardingViewPagerAdapter : RecyclerView.Adapter<PagerVH2>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH2 {
        return PagerVH2(
            SplashScreenItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = MAX_STEP

    override fun onBindViewHolder(holder: PagerVH2, position: Int) =

        holder.itemView.run {

            with(holder) {
                if (position == 0) {
                    binding.introDescription.text =
                        context.getString(R.string.intro_description_1)
                    binding.introImage.setImageResource(R.drawable.onboarding1)
                }
                if (position == 1) {
                    binding.introDescription.text =
                        context.getString(R.string.intro_description_2)
                    binding.introImage.setImageResource(R.drawable.onboarding2)
                }
                if (position == 2) {
                    binding.introDescription.text =
                        context.getString(R.string.intro_description_3)
                    binding.introImage.setImageResource(R.drawable.onboarding3)
                }
                if (position == 3) {
                    binding.introDescription.text =
                        context.getString(R.string.intro_description_4)
                    binding.introImage.setImageResource(R.drawable.onboarding4)
                }
            }
        }
}

class PagerVH2(val binding: SplashScreenItemBinding) : RecyclerView.ViewHolder(binding.root)