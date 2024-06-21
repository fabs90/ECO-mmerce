package com.example.ecommerce.view.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.ecommerce.data.FavoriteProduct
import com.example.ecommerce.data.FavoriteProductDao
import com.example.ecommerce.data.ProductDatabase

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private var productDao : FavoriteProductDao?
    private var productDb : ProductDatabase?

    init{
        productDb = ProductDatabase.getDatabase(application)
        productDao = productDb?.favoriteUserDao()
    }

    fun getFavoriteUser() : LiveData<List<FavoriteProduct>>?{
        return productDao?.getFavoriteProduct()
    }
}