package com.example.shopapp.repository

import com.example.shopapp.models.AuthResponse
import com.example.shopapp.models.LoginResponse
import com.example.shopapp.models.Profile
import com.example.shopapp.models.RegisterResponse
import com.example.shopapp.utils.Resource

interface AuthRepository {
    suspend fun registerUser(
        name: String,
        phone: String,
        email: String,
        password: String,
        image: String
    ): Resource<AuthResponse<RegisterResponse>>

    suspend fun loginUser(
        email: String,
        password: String
    ): Resource<AuthResponse<LoginResponse>>

    suspend fun getUserProfileInfo(): Resource<AuthResponse<Profile>>
}