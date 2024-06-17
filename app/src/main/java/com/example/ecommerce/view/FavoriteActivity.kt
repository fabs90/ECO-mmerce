package com.example.ecommerce.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.databinding.ActivityFavoriteBinding
import com.example.ecommerce.view.adapter.FavoriteAdapter
import com.example.ecommerce.view.data.api.ProductsItem
import com.example.ecommerce.view.data.local.FavoriteProduct
import com.example.ecommerce.view.model.FavoriteViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FavoriteActivity : AppCompatActivity() {

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        adapter = FavoriteAdapter().apply {
            setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ProductsItem) {
                    //navigateToDetail(data)
                }
            })
        }
        binding.recyclerViewFavorite.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            adapter = this@FavoriteActivity.adapter
        }

        // Use coroutines to fetch favorite products
        CoroutineScope(Dispatchers.Main).launch {
            val favoriteProducts = withContext(Dispatchers.IO) {
                viewModel.getFavoriteProduct()
            }
            favoriteProducts?.let {
                val list = mapFavoriteProductToProductList(it)
                adapter.setList(list)
            }
        }
    }

    private fun mapFavoriteProductToProductList(favoriteProducts: List<FavoriteProduct>): List<ProductsItem> {
        return favoriteProducts.map { product ->
            ProductsItem(product.id, product.name, product.price, product.image)
        }
    }

    private fun ProductsItem(
        image: String,
        createdAt: String,
        price: String,
        name: String
    ): ProductsItem {
            TODO("Not yet implemented")
    }
}