package com.example.ecommerce.view.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavoriteProduct(product: FavoriteProduct)

    @Query("SELECT * FROM favorite_product")
    suspend fun getFavoriteProducts(): List<FavoriteProduct>

    @Query("DELETE FROM favorite_product WHERE id = :id")
    suspend fun removeFavoriteProduct(id: String)

    @Query("SELECT COUNT(*) FROM favorite_product WHERE id = :id")
    suspend fun checkFavorite(id: String): Int
}