package com.example.shopapp.repository

import com.example.shopapp.models.AuthResponse
import com.example.shopapp.models.Banner
import com.example.shopapp.models.CartItemData
import com.example.shopapp.models.Category
import com.example.shopapp.models.Complaint
import com.example.shopapp.models.Data
import com.example.shopapp.models.Faqs
import com.example.shopapp.models.FavouriteItem
import com.example.shopapp.models.MyResponse
import com.example.shopapp.models.Notifications
import com.example.shopapp.models.Product
import com.example.shopapp.models.Settings
import com.example.shopapp.utils.Resource
import retrofit2.http.Query

interface ApiRepo {
    suspend fun getAllCategories(): Resource<MyResponse<Data<Category>>>
    suspend fun getProductsByCategoryID(id: Int): Resource<MyResponse<Data<Product>>>
    suspend fun getAllProducts(): Resource<AuthResponse<Data<Product>>>
    suspend fun searchForProducts(text: String):Resource<AuthResponse<Data<Product>>>

    suspend fun getBanners(): Resource<Banner>
    suspend fun getAllCartItems(): Resource<AuthResponse<CartItemData>>
    suspend fun getFavouriteItems(): Resource<AuthResponse<Data<FavouriteItem>>>

    suspend fun addItemToCart(product_id: Int): Resource<AuthResponse<FavouriteItem>>

    suspend fun deleteItemFromCarts(id: Int): Resource<AuthResponse<CartItemData>>

    suspend fun updateCartItemQuantity(id: Int, quantity: Int): Resource<AuthResponse<CartItemData>>

    //Settings
    suspend fun getSettings(): Resource<AuthResponse<Settings>>

    // faqs
    suspend fun getFaqs(): Resource<AuthResponse<Data<Faqs>>>

    //complaints
    suspend fun makeComplaints(complaint: Complaint): Resource<AuthResponse<Complaint>>

    //notifications
    suspend fun getNotifications(): Resource<AuthResponse<Data<Notifications>>>


    suspend fun addItemToFavourites(product_id: Int): Resource<AuthResponse<FavouriteItem>>
    suspend fun deleteItemFromFavourites(id: Int): Resource<AuthResponse<FavouriteItem>>

}