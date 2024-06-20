package com.example.ecommerce.view.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.ecommerce.data.FavoriteProduct
import com.example.ecommerce.data.FavoriteProductDao
import com.example.ecommerce.data.ProductDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) :  AndroidViewModel(application) {

    private var productDao: FavoriteProductDao?
    private var productDb : ProductDatabase? = ProductDatabase.getDatabase(application)

    init{
        productDao = productDb?.favoriteUserDao()
    }

    fun addToFavorite(id: String, image: String, name: String, description: String){
        CoroutineScope(Dispatchers.IO).launch {
            val product = FavoriteProduct(
                id,
                image,
                name,
                description,
            )
            productDao?.addToFavorite(product)
        }
    }

    suspend fun checkProduct(id : String) = productDao?.checkProduct(id)

    fun removeFromFavorite(id : String){
        CoroutineScope(Dispatchers.IO).launch {
            productDao?.removeFromFavorite(id)
        }
    }
}