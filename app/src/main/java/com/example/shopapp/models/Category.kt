package com.example.shopapp.models

import java.io.Serializable

data class Category (
    var id: Int? = null,
    var name: String? = null,
    var image: String? = null,
): Serializable