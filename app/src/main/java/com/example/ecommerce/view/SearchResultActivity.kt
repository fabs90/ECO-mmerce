package com.example.ecommerce.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ActivitySearchResultBinding
import com.example.ecommerce.view.adapter.ProductAdapter
import com.example.ecommerce.view.data.api.ApiConfig
import com.example.ecommerce.view.data.response.ProductsResponse
import com.example.ecommerce.view.detail.DetailActivity
import com.example.ecommerce.view.favorite.FavoriteActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchResultBinding
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()

        productAdapter = ProductAdapter(this, listOf()) { product ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("product_id", product.id)
            startActivity(intent)
        }

        binding.recyclerViewSearch.apply {
            layoutManager = GridLayoutManager(this@SearchResultActivity, 2)
            adapter = productAdapter
        }

        val query = intent.getStringExtra("search_query")
        if (!query.isNullOrEmpty()) {
            searchProducts(query)
        }
    }

    private fun searchProducts(query: String) {
        val call = ApiConfig.apiService().getProducts()
        call.enqueue(object : Callback<ProductsResponse> {
            override fun onResponse(call: Call<ProductsResponse>, response: Response<ProductsResponse>) {
                if (response.isSuccessful) {
                    val products = response.body()?.products?.filter {
                        it.name.contains(query, ignoreCase = true)
                    } ?: emptyList()
                    productAdapter.updateProductList(products)
                } else {
                    Toast.makeText(this@SearchResultActivity, "Failed to load products", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                Toast.makeText(this@SearchResultActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupNavigation() {
        // Logic for setting up the bottom navigation
        val navView: BottomNavigationView = binding.bottomNavigation
        navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    Log.d("com.example.ecommerce.view.MainActivity", "Home selected")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.navigation_profile -> {
                    Log.d("com.example.ecommerce.view.MainActivity", "Profile selected")
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.navigation_favorite -> {
                    Log.d("com.example.ecommerce.view.MainActivity", "Favorite selected")
                    val intent = Intent(this, FavoriteActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> {
                    false
                }
            }
        }
    }
}
