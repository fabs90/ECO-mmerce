package com.example.ecommerce.view.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ecommerce.data.FavoriteProduct
import com.example.ecommerce.data.FavoriteProductDao
import com.example.ecommerce.data.ProductDatabase
import com.example.ecommerce.view.data.api.ProductsItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private var productDao: FavoriteProductDao?
    private var productDb: ProductDatabase? = ProductDatabase.getDatabase(application)

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val _selectedProduct = MutableLiveData<ProductsItem>()
    val selectedProduct: LiveData<ProductsItem>
        get() = _selectedProduct


    init {
        productDao = productDb?.favoriteUserDao()
    }

    fun selectProduct(product: ProductsItem) {
        _selectedProduct.value = product
    }

    fun addToFavorite(image: String, name: String, description: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("DetailViewModel", "Adding to favorite: $name")
                val product = FavoriteProduct(
                    image = image,
                    name = name,
                    description = description,
                )
                productDao?.addToFavorite(product)
                _isFavorite.postValue(true)
                Log.d("DetailViewModel", "Successfully added to favorite: $name")
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error adding to favorite: ${e.message}")
            }
        }
    }

    fun checkProduct(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val count = productDao?.checkProduct(name) ?: 0
                _isFavorite.postValue(count > 0)
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error checking product: ${e.message}")
                _isFavorite.postValue(false)
            }
        }
    }

    fun removeFromFavorite(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("DetailViewModel", "Removing from favorite: $name")
                productDao?.removeFromFavorite(name)
                _isFavorite.postValue(false)
                Log.d("DetailViewModel", "Successfully removed from favorite: $name")
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error removing from favorite: ${e.message}")
            }
        }
    }
}
