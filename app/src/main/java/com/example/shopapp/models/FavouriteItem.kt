package com.example.shopapp.models

data class FavouriteItem(
    val id: Int,
    val quantity: Int? = null,
    val product: Product
)