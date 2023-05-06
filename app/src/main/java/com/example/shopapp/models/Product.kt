package com.example.shopapp.models

import java.io.Serializable

data class Product(
    val description: String?="",
    val discount: Int?=null,
    val id: Int?=null,
    val image: String?="",
    val images: List<String>?=null,
    val in_cart: Boolean?=false,
    val in_favorites: Boolean?=false,
    val name: String?="",
    val old_price: Double?=null,
    val price: Double?=null,
    val star:String?=null
): Serializable