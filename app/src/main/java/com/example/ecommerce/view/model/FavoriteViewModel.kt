package com.example.ecommerce.view.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.ecommerce.view.data.local.FavoriteDatabase
import com.example.ecommerce.view.data.local.FavoriteProduct
import com.example.ecommerce.view.data.local.FavoriteProductDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private var favoriteDao : FavoriteProductDao? = null
    private var favoriteDb : FavoriteDatabase? = null

    init{
        favoriteDb = FavoriteDatabase.getDatabase(application)
        favoriteDao = favoriteDb?.favoriteProductDao()
    }
    suspend fun getFavoriteProduct(): List<FavoriteProduct>? {
        return withContext(Dispatchers.IO) {
            favoriteDao?.getFavoriteProducts()
        }
    }
}