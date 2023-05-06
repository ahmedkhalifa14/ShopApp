package com.example.shopapp.models

data class AuthResponse<T>(
    val data:T?=null,
    val message: String,
    val status: Boolean
)

//AuthResponse<Data<FavouriteItem>>