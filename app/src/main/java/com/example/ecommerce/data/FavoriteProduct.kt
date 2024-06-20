package com.example.ecommerce.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_products")
data class FavoriteProduct(
    @PrimaryKey val id: String,
    val image: String,
    val name: String,
    val description: String
)
