package com.example.shopapp.models

import com.google.gson.annotations.SerializedName

data class Banner(
    @SerializedName("status")
    val status:Boolean=false,
    @SerializedName("message")
    val message:String,
    @SerializedName("data")
    val data:List<BannerData>?=null
)
data class BannerData(
    var id: Int? = null,
    var image: String? = null,
    var category: Category? = null,
    var product: Product? = null
)