package com.example.shopapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopapp.databinding.SearchItemBinding
import com.example.shopapp.models.Product

class SearchAdapter () :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    var products: List<Product>
        get() = differ.currentList
        set(value) = differ.submitList(value)
    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }
    }
    private val differ = AsyncListDiffer(this, diffCallback)

    inner class SearchViewHolder(private val itemBinding: SearchItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindData(product: Product){
            itemBinding.root.apply {
                Glide.with(context).load(product.image).into(itemBinding.searchItemImg)
                itemBinding.searchItemName.text=product.name
                itemBinding.searchItemPrice.text="EGP"+product.price.toString()
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemBinding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(itemBinding)
    }
    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val product = products[position]
        holder.apply {
            bindData(product)
        }
    }


}