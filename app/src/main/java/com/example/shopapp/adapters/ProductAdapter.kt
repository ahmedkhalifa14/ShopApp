package com.example.shopapp.adapters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopapp.R
import com.example.shopapp.databinding.CartItemBinding
import com.example.shopapp.databinding.FavouriteItemBinding
import com.example.shopapp.models.Product

class ProductAdapter(private val type: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ViewType {
        TYPE_ONE,
        TYPE_TWO
    }

    inner class HomeViewHolder(val homeBinding: FavouriteItemBinding) :
        RecyclerView.ViewHolder(homeBinding.root)

    inner class ProductViewHolder(val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.TYPE_ONE.ordinal -> {
                HomeViewHolder(
                    FavouriteItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )

            }

            ViewType.TYPE_TWO.ordinal -> {
                ProductViewHolder(
                    CartItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = differ.currentList[position]
        when (holder.itemViewType) {
            ViewType.TYPE_ONE.ordinal -> {
                val homeViewHolder = holder as HomeViewHolder
                homeViewHolder.itemView.apply {
                    homeViewHolder.homeBinding.productNameTv.text = product.name
                    // homeViewHolder.homeBinding..text=product.description
                    Glide.with(this).load(product.image).into(homeViewHolder.homeBinding.productImg)
                    homeViewHolder.homeBinding.productPriceTv.text =
                        "EGP " + product.price.toString()
                    setOnClickListener {
                        onItemClickListener?.let { it(product) }
                    }

                }

            }

            ViewType.TYPE_TWO.ordinal -> {
                val productViewHolder = holder as ProductViewHolder
                productViewHolder.itemView.apply {
                    productViewHolder.binding.itemName.text = product.name
                    productViewHolder.binding.itemDescription.text = product.description
                    productViewHolder.binding.quantityLayout.visibility = View.GONE
                    if (product.in_cart == true) {
                        // productViewHolder.binding.addToCart.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.secondColor))
                        productViewHolder.binding.addToCart.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.secondColor
                            ), PorterDuff.Mode.SRC_IN
                        )

                    } else {
                        productViewHolder.binding.addToCart.setColorFilter(
                            ContextCompat.getColor(
                                context,
                                R.color.black
                            ), PorterDuff.Mode.SRC_IN
                        )

                    }


                    Glide.with(this).load(product.image).into(productViewHolder.binding.cartImg)
                    productViewHolder.binding.itemPrice.text = "EGP " + product.price.toString()
                    productViewHolder.binding.addToCart.setOnClickListener {
                        if (product.in_cart == false) {
                            productViewHolder.binding.addToCart.setColorFilter(
                                ContextCompat.getColor(
                                    context,
                                    R.color.secondColor
                                ), PorterDuff.Mode.SRC_IN
                            )

                        } else {
                            productViewHolder.binding.addToCart.setColorFilter(
                                ContextCompat.getColor(
                                    context,
                                    R.color.black
                                ), PorterDuff.Mode.SRC_IN
                            )
                        }
                        onAddToCartClickListener?.let { it(product) }
                    }

                    setOnClickListener {
                        onItemClickListener?.let { it(product) }
                    }

                }
            }
        }

    }

    override fun getItemCount(): Int = differ.currentList.size

    private var onItemClickListener: ((Product) -> Unit)? = null
    fun setOnItemClickListener(listener: (Product) -> Unit) {
        onItemClickListener = listener
    }

    private var onAddToCartClickListener: ((Product) -> Unit)? = null
    fun setOnAddToCartItemClickListener(listener: (Product) -> Unit) {
        onAddToCartClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        //return if (position % 2 == 0) TYPE_VIEWHOLDER_ONE else TYPE_VIEWHOLDER_TWO

        return when (type) {
            1 -> {
                ViewType.TYPE_ONE.ordinal
            }

            2 -> {
                ViewType.TYPE_TWO.ordinal
            }

            else -> throw IllegalArgumentException("Invalid value")

        }
    }
}