package com.teamvault.smartgrocer.model

data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val category: String = "",
    val isAvailable: Boolean = true,
    val imageUrl: String = ""
)