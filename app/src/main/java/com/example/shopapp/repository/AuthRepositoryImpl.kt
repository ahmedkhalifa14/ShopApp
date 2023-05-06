package com.example.shopapp.repository

import com.example.shopapp.data.network.ApiService
import com.example.shopapp.models.AuthResponse
import com.example.shopapp.models.LoginResponse
import com.example.shopapp.models.Profile
import com.example.shopapp.models.RegisterResponse
import com.example.shopapp.models.User
import com.example.shopapp.utils.Resource
import com.example.shopapp.utils.Utils.tryCatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : AuthRepository {
    override suspend fun registerUser(
        name: String,
        phone: String,
        email: String,
        password: String,
        image: String,
    ): Resource<AuthResponse<RegisterResponse>> =
        withContext(Dispatchers.IO) {
            tryCatch {
                val user = User(  name = name,
                    phone = phone,
                    email = email,
                    password = password,
                    image = image
                )
                val result = apiService.registerUser(user)
                Resource.Success(result)
            }
        }


    override suspend fun loginUser(
        email: String,
        password: String
    ): Resource<AuthResponse<LoginResponse>> =
        withContext(Dispatchers.IO) {
            tryCatch {
                val loginData = HashMap<String, String>()
                loginData["email"] = email
                loginData["password"] = password
                val result = apiService.loginUser(loginData)
                Resource.Success(result)
            }
        }

    override suspend fun getUserProfileInfo(): Resource<AuthResponse<Profile>> =
        withContext(Dispatchers.IO){
            tryCatch {
                val result = apiService.getUserProfileInfo()
                Resource.Success(result)
            }
        }

}