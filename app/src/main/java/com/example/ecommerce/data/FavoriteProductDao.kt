package com.example.ecommerce.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteProductDao {
    @Insert
    fun addToFavorite(favoriteProduct: FavoriteProduct)

    @Query("SELECT * FROM favorite_products")
    fun getFavoriteProduct(): LiveData<List<FavoriteProduct>>

    @Query("SELECT count(*) FROM favorite_products WHERE favorite_products.name = :name")
    fun checkProduct(name: String): Int

    @Query("DELETE FROM favorite_products WHERE favorite_products.name = :name")
    fun removeFromFavorite(name: String): Int
}
