package com.example.shopapp.models

data class RegisterResponse(
    val email: String,
    val id: Int,
    val image: String,
    val name: String,
    val phone: String,
    val token: String
)