package com.example.shopapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopapp.R
import com.example.shopapp.databinding.FavouriteItemBinding
import com.example.shopapp.models.FavouriteItem

class FavouriteItemsAdapter () :
    RecyclerView.Adapter<FavouriteItemsAdapter.FavouriteViewHolder>() {
     var favourites: List<FavouriteItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private val diffCallback = object : DiffUtil.ItemCallback<FavouriteItem>() {
        override fun areContentsTheSame(oldItem: FavouriteItem, newItem: FavouriteItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areItemsTheSame(oldItem: FavouriteItem, newItem: FavouriteItem): Boolean {
            return oldItem.id == newItem.id
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)

    inner class FavouriteViewHolder(private val itemBinding: FavouriteItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
            fun bindData(favItem:FavouriteItem){
                itemBinding.root.apply {
                    Glide.with(context).load(favItem.product.image).into(itemBinding.productImg)
                    itemBinding.productNameTv.text=favItem.product.name
                    itemBinding.productPriceTv.text=favItem.product.price.toString()
                    itemBinding.favIcon.setImageResource(R.drawable.fav)
                    itemBinding.favIcon.setOnClickListener {
                        onFavIconItemClickListener?.let { item ->
                            item(favItem)
                        }
                    }
                }

            }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val itemBinding =
            FavouriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavouriteViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = favourites.size

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val favItem = favourites[position]
        holder.apply {
            bindData(favItem)
        }
    }


    private var onFavIconItemClickListener: ((FavouriteItem) -> Unit)? = null
    fun setOnFavItemClickListener(listener: (FavouriteItem) -> Unit) {
        onFavIconItemClickListener = listener
    }

}