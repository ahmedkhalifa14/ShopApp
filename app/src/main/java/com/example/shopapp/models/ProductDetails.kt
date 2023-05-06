package com.example.shopapp.models

import java.io.Serializable

data class ProductDetails (
    val product: Product,
    val category_id:Int
        ): Serializable