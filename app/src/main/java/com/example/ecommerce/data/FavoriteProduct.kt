package com.example.ecommerce.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite_products")
data class FavoriteProduct(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,  // Auto-generate the primary key
    val image: String,
    val name: String,
    val description: String
) : Serializable
