package com.example.shopapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopapp.databinding.CartItem2Binding
import com.example.shopapp.models.CartItem


class CartAdapter () :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    inner class CartViewHolder(private val cartItemBinding: CartItem2Binding) :
        RecyclerView.ViewHolder(cartItemBinding.root) {
        fun bindData(cartItem: CartItem) {
            cartItemBinding.root.apply {
                setOnClickListener {
                    onItemClickListener?.let { it(cartItem) }
                }
                cartItemBinding.delete.setOnClickListener {
                    onItemClickListener?.let { it(cartItem) }
                }

                cartItemBinding.plus.setOnClickListener {
                    onItemCounterClickListener?.let { item ->
                        item(handelClickOnPlusAndMinus(false, cartItem), adapterPosition)
                    }
                }
                cartItemBinding.minus.setOnClickListener {
                    onItemCounterClickListener?.let { item ->
                        item(handelClickOnPlusAndMinus(true, cartItem), adapterPosition)

                    }
                }
                Glide.with(context).load(cartItem.product.image).into(cartItemBinding.cartItemImg)
                cartItemBinding.cartItemName.text = cartItem.product.name
                cartItemBinding.cartItemPrice.text ="EGP "+cartItem.product.price.toString()
                cartItemBinding.cartItemTotalPrice.text=":EGP "+(cartItem.product.price?.times(
                    cartItem.quantity
                )).toString()

                cartItemBinding.itemCartCounterTv.text = cartItem.quantity.toString()

            }

        }

        private fun handelClickOnPlusAndMinus(minis: Boolean, cartItem: CartItem): CartItem {
            var counter = cartItemBinding.itemCartCounterTv.text.toString().toInt()
            val itemPrice = cartItem.product.price

            if (minis) {
                if (counter <= 0) {
                    return cartItem
                } else {
                    counter -= 1
                }
            } else {
                counter += 1
            }

            cartItemBinding.itemCartCounterTv.text = counter.toString()
          //  cartItem.quantity = counter
          //  cartItem.productTotalPrice= itemPrice * counter
           // cartItem.productQuantity = cartItem.productQuantity
            cartItem.quantity=counter
           // cartItemBinding.cartItemPrice.text = cartItem.productTotalPrice.toString()
          //  Log.d("cartAdapter","productPrice "+ cartItem.productPrice.toString())
           // Log.d("cartAdapter",  "productQuantity "+ cartItem.productQuantity.toString())
           // Log.d("cartAdapter","itemPrice"+ itemPrice.toString())
            return cartItem
        }
    }


    private var onItemClickListener: ((CartItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (CartItem) -> Unit) {
        onItemClickListener = listener
    }


    private var onItemLongClickListener: ((CartItem) -> Unit)? = null
    fun setOnItemLongClickListener(listener: (CartItem) -> Unit) {
        onItemLongClickListener = listener
    }


    private var onItemCounterClickListener: ((CartItem, Int) -> Unit)? = null

    fun setOnItemCounterClickListener(listener: (CartItem, Int) -> Unit) {
        onItemCounterClickListener = listener
    }


    var cartList: MutableList<CartItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)
    val totalPrice: Float = 0.0f
    private val diffCallback = object : DiffUtil.ItemCallback<CartItem>() {
        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }


        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.CartViewHolder {
        val cartItemBinding =
            CartItem2Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return CartViewHolder(cartItemBinding)
    }

    override fun onBindViewHolder(holder: CartAdapter.CartViewHolder, position: Int) {
        val cartItem = cartList[position]
        holder.apply {
            bindData(cartItem)
        }
    }

    override fun getItemCount(): Int = cartList.size

}


