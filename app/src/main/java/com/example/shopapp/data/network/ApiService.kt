package com.example.shopapp.data.network

import com.example.shopapp.models.*
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("login")
    suspend fun loginUser(@Body loginData: HashMap<String, String>): AuthResponse<LoginResponse>

    @POST("register")
    suspend fun registerUser(@Body user: User): AuthResponse<RegisterResponse>

    //profileData
    @GET("profile")
    suspend fun getUserProfileInfo(): AuthResponse<Profile>

    //cart
    @GET("carts")
    suspend fun getAllCartItems(): AuthResponse<CartItemData>

    @POST("carts")
    suspend fun addItemToCart(@Query ("product_id") product_id : Int): AuthResponse<FavouriteItem>

    @DELETE("cart/{product_id}")
    suspend fun deleteItemFromCarts(@Path("product_id") id: Int): AuthResponse<CartItemData>

    @PUT("cart/{product_id}")
    suspend fun updateCartItemQuantity(
        @Body quantity: Int,
        @Path("product_id") id: Int
    ): AuthResponse<CartItemData>

    //favorites
    @GET("favorites")
    suspend fun getFavouriteItems(): AuthResponse<Data<FavouriteItem>>

    @POST("favorites")
    suspend fun addItemToFavourites(@Body product_id: Int): AuthResponse<FavouriteItem>

    @DELETE("favorites/{product_id}")
    suspend fun deleteItemFromFavourites(@Path("product_id") id: Int): AuthResponse<FavouriteItem>


    //categories and products
    @GET("categories")
    suspend fun getAllCategories(): MyResponse<Data<Category>>

    @GET("categories/{category_id}")
    suspend fun getProductsByCategoryID(@Path("category_id") id: Int): MyResponse<Data<Product>>
    @GET("products")
    suspend fun getAllProducts():AuthResponse<Data<Product>>

    @GET("banners")
    suspend fun getBanners(): Banner


    //Settings
    @GET("settings")
    suspend fun getSettings(): AuthResponse<Settings>

    // faqs
    @GET("faqs")
    suspend fun getFaqs(): AuthResponse<Data<Faqs>>

    //complaints
    @POST("complaints")
    suspend fun makeComplaints(@Body complaint: Complaint): AuthResponse<Complaint>

    //notifications
    @GET("notifications")
    suspend fun getNotifications(): AuthResponse<Data<Notifications>>

}