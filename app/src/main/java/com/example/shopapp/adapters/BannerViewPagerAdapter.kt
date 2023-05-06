package com.example.shopapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopapp.databinding.BannerLayoutBinding
import com.example.shopapp.models.BannerData

class BannerViewPagerAdapter() :
    RecyclerView.Adapter<BannerViewPagerAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: BannerLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<BannerData>() {
        override fun areItemsTheSame(oldItem: BannerData, newItem: BannerData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BannerData, newItem: BannerData): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BannerLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val banner = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(banner.image).into(holder.binding.bannerImage)
        }


    }
}