package com.example.ecommerce.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ActivitySearchResultBinding
import com.example.ecommerce.view.adapter.ProductAdapter
import com.example.ecommerce.view.adapter.RecommendationAdapter
import com.example.ecommerce.view.data.api.ApiConfig
import com.example.ecommerce.view.data.response.ProductsResponse
import com.example.ecommerce.view.data.response.RecommendResponse
import com.example.ecommerce.view.detail.DetailActivity
import com.example.ecommerce.view.favorite.FavoriteActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class SearchResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchResultBinding
    private lateinit var productAdapter: ProductAdapter
    private lateinit var recommendationAdapter: RecommendationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(true)
        setupNavigation()

        productAdapter = ProductAdapter(this, listOf()) { product ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("product_id", product.id)
            startActivity(intent)
        }

        recommendationAdapter = RecommendationAdapter(getDefaultRecommendations()) // Initialize with default recommendations

        binding.recyclerViewSearch.apply {
            layoutManager = GridLayoutManager(this@SearchResultActivity, 2)
            adapter = productAdapter
        }

        binding.recyclerViewRecommendations.apply {
            layoutManager = GridLayoutManager(this@SearchResultActivity, 2)
            adapter = recommendationAdapter
        }

        val query = intent.getStringExtra("search_query")
        if (!query.isNullOrEmpty()) {
            searchProducts(query)
        }
    }

    private fun searchProducts(query: String) {
        showLoading(false)
        val call = ApiConfig.apiService().getProducts()
        call.enqueue(object : Callback<ProductsResponse> {
            override fun onResponse(call: Call<ProductsResponse>, response: Response<ProductsResponse>) {
                if (response.isSuccessful) {
                    val products = response.body()?.products?.filter {
                        it.name.contains(query, ignoreCase = true)
                    } ?: emptyList()

                    if (products.isEmpty()) {
                        Toast.makeText(this@SearchResultActivity, "No products found😔", Toast.LENGTH_SHORT).show()
                    }
                    productAdapter.updateProductList(products)
                    // Get recommendations for each product
                    products.forEach { product ->
                        getAllRecommendations(product.id)
                    }
                } else {
                    Toast.makeText(this@SearchResultActivity, "Failed to load products", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                Toast.makeText(this@SearchResultActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("SearchResultActivity", "Failed to fetch products", t)
                showLoading(false) // Ensure loading indicator is hidden on failure
            }
        })
    }

    private fun getAllRecommendations(productId: String) {
        val call = ApiConfig.apiService().getRecommend(productId)
        call.enqueue(object : Callback<List<RecommendResponse>> {
            override fun onResponse(call: Call<List<RecommendResponse>>, response: Response<List<RecommendResponse>>) {
                if (response.isSuccessful) {
                    val recommendations = response.body() ?: emptyList()
                    if (recommendations.isEmpty()) {
                        recommendationAdapter.updateRecommendationList(getDefaultRecommendations())
                        recommendationAdapter.notifyDataSetChanged() // Notify adapter of dataset change
                    } else {
                        recommendationAdapter.updateRecommendationList(recommendations)
                        recommendationAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<List<RecommendResponse>>, t: Throwable) {
                Log.e("SearchResultActivity", "Error: ${t.message}")
                Toast.makeText(this@SearchResultActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getDefaultRecommendations(): List<RecommendResponse> {
        return listOf(
            RecommendResponse(
                input_ids = "f449ec65dcbc041b6ae5e6a32717d01b",
                output_score = 0.04885903745889664,
                product_name = "AW Bellies",
                product_url = "http://www.flipkart.com/aw-bellies/p/itmeh4grgfbkexnt?pid=SHOEH4GRSUBJGZXE"
            ),
            RecommendResponse(
                input_ids = "83c53f8948f508f51d2249b489ca8e7d",
                output_score = 0.02802414819598198,
                product_name = "Freelance Vacuum Bottles 350 ml Bottle",
                product_url = "http://www.flipkart.com/freelance-vacuum-bottles-350-ml-bottle/p/itmegytzpkgmrew5?pid=BOTEGYTZ2T6WUJMM"
            ),
            RecommendResponse(
                input_ids = "7db2acfee8298ef8c687d688147c69be",
                output_score = 0.009491175413131714,
                product_name = "Ligans NY Formal Black Belt",
                product_url = "http://www.flipkart.com/ligans-ny-men-formal-black-genuine-leather-belt/p/itmeg68tjkg9zgzf?pid=BELEG68SBMZFFN5Z"
            )
        )
    }

    private fun setupNavigation() {
        val navView: BottomNavigationView = binding.bottomNavigation
        navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this@SearchResultActivity, MainActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this@SearchResultActivity, ProfileActivity::class.java))
                    true
                }
                R.id.navigation_favorite -> {
                    startActivity(Intent(this@SearchResultActivity, FavoriteActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun showLoading(status: Boolean) {
        binding.progressBar4.visibility = if (status) View.VISIBLE else View.GONE
    }
}
