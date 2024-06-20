package com.example.ecommerce.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteProductDao {
    @Insert
    suspend fun addToFavorite(favoriteProduct: FavoriteProduct)

    @Query("SELECT * FROM favorite_products ORDER BY id ASC")
    fun getFavoriteProduct() : LiveData<List<FavoriteProduct>>

    @Query("SELECT count(*) FROM favorite_products WHERE id = :id")
    suspend fun checkProduct(id : String) : Int

    @Query("DELETE FROM favorite_products WHERE id = :id")
    suspend fun removeFromFavorite(id : String) : Int
}