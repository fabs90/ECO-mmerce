package com.example.ecommerce.view.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.view.data.api.ProductsItem
import com.example.ecommerce.view.data.local.FavoriteDatabase
import com.example.ecommerce.view.data.local.FavoriteProduct
import com.example.ecommerce.view.data.local.FavoriteProductDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel (application: Application) : AndroidViewModel(application) {
    private val _product = MutableLiveData<ProductsItem>()
    val product: LiveData<ProductsItem>
        get() = _product

    private val productDao: FavoriteProductDao?= FavoriteDatabase.getDatabase(application)?.favoriteProductDao()

     fun addToFavorite(id: String,name: String, price: String, image: String) {
         viewModelScope.launch(Dispatchers.IO) {
             val id = FavoriteProduct(
                 id,
                 name,
                 price,
                 image
             )
             productDao?.addFavoriteProduct(id)
         }
     }
    suspend fun checkFavorite(id: String) = productDao?.checkFavorite(id)

    fun removeFromFavorite(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            productDao?.removeFavoriteProduct(id)
        }
    }
}