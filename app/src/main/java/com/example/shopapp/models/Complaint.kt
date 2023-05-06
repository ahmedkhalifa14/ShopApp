package com.example.shopapp.models

data class Complaint(
    val name: String,
    val phone: String,
    val email: String,
    val message: String,
    val id: Int? = null,
)
