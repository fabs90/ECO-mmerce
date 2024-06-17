package com.example.ecommerce.view.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorite_product")
data class FavoriteProduct(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: String,
    val name: String,
    val price: Double,
    val image: String
)