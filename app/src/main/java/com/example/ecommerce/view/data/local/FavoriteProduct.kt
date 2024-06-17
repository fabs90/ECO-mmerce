package com.example.ecommerce.view.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "favorite_product")
data class FavoriteProduct(
    val id: String,
    @PrimaryKey
    val name: String,
    val price: String,
    val image: String
): Serializable