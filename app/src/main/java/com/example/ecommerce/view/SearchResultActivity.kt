package com.example.ecommerce.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ActivitySearchResultBinding
import com.example.ecommerce.view.adapter.ProductAdapter
import com.example.ecommerce.view.data.api.ApiConfig
import com.example.ecommerce.view.data.response.ProductsResponse
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
}
