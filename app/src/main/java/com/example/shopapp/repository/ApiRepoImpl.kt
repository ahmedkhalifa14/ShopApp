package com.example.shopapp.repository

import com.example.shopapp.data.network.ApiService
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
import com.example.shopapp.utils.Utils.tryCatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApiRepoImpl @Inject constructor(
    private val apiService: ApiService
) : ApiRepo {
    override suspend fun getAllCategories(): Resource<MyResponse<Data<Category>>> =
        withContext(Dispatchers.IO) {
            tryCatch {
                val result = apiService.getAllCategories()
                Resource.Success(result)
            }
        }

    override suspend fun getProductsByCategoryID(id: Int): Resource<MyResponse<Data<Product>>> =
        withContext(Dispatchers.IO) {
            tryCatch {
                val result = apiService.getProductsByCategoryID(id)
                Resource.Success(result)
            }
        }

    override suspend fun getAllProducts(): Resource<AuthResponse<Data<Product>>> =
        withContext(Dispatchers.IO) {
            tryCatch {
                val result = apiService.getAllProducts()
                Resource.Success(result)
            }
        }

    override suspend fun searchForProducts(text: String): Resource<AuthResponse<Data<Product>>> =
        withContext(Dispatchers.IO) {
            tryCatch {
                val result = apiService.searchForProducts(text)
                Resource.Success(result)
            }
        }

    override suspend fun getBanners(): Resource<Banner> =
        withContext(Dispatchers.IO) {
            tryCatch {
                val result = apiService.getBanners()
                Resource.Success(result)
            }
        }

    override suspend fun getAllCartItems(): Resource<AuthResponse<CartItemData>> =
        withContext(Dispatchers.IO) {
            tryCatch {
                val result = apiService.getAllCartItems()
                Resource.Success(result)
            }
        }

    override suspend fun getFavouriteItems(): Resource<AuthResponse<Data<FavouriteItem>>> =
        withContext(Dispatchers.IO) {
            val result = apiService.getFavouriteItems()
            Resource.Success(result)
        }

    override suspend fun addItemToCart(product_id: Int): Resource<AuthResponse<FavouriteItem>> =
        withContext(Dispatchers.IO) {
            val result = apiService.addItemToCart(product_id)
            Resource.Success(result)
        }

    override suspend fun deleteItemFromCarts(id: Int): Resource<AuthResponse<CartItemData>> =
        withContext(Dispatchers.IO) {
            val result = apiService.deleteItemFromCarts(id)
            Resource.Success(result)
        }

    override suspend fun updateCartItemQuantity(
        id: Int,
        quantity: Int

    ): Resource<AuthResponse<CartItemData>> =
        withContext(Dispatchers.IO) {
            val result = apiService.updateCartItemQuantity(id, quantity)
            Resource.Success(result)
        }

    override suspend fun getSettings(): Resource<AuthResponse<Settings>> =
        withContext(Dispatchers.IO) {
            val result = apiService.getSettings()
            Resource.Success(result)
        }

    override suspend fun getFaqs(): Resource<AuthResponse<Data<Faqs>>> =
        withContext(Dispatchers.IO) {
            val result = apiService.getFaqs()
            Resource.Success(result)
        }

    override suspend fun makeComplaints(complaint: Complaint): Resource<AuthResponse<Complaint>> =
        withContext(Dispatchers.IO) {
            val result = apiService.makeComplaints(complaint)
            Resource.Success(result)
        }

    override suspend fun getNotifications(): Resource<AuthResponse<Data<Notifications>>> =
        withContext(Dispatchers.IO) {
            val result = apiService.getNotifications()
            Resource.Success(result)
        }

    override suspend fun addItemToFavourites(product_id: Int): Resource<AuthResponse<FavouriteItem>> =
        withContext(Dispatchers.IO) {
            val result = apiService.addItemToFavourites(product_id)
            Resource.Success(result)
        }

    override suspend fun deleteItemFromFavourites(id: Int): Resource<AuthResponse<FavouriteItem>> =
        withContext(Dispatchers.IO) {
            val result = apiService.deleteItemFromFavourites(id)
            Resource.Success(result)
        }
}
