package com.example.ecommerce.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ActivityDetailBinding
import com.example.ecommerce.view.data.api.ApiConfig
import com.example.ecommerce.view.data.response.ProductsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val productId = intent.getStringExtra("product_id")
        if (productId != null) {
            // Fetch and display product details using the productId
            fetchProductDetails(productId)
        }
    }

    private fun fetchProductDetails(productId: String) {
        val call = ApiConfig.apiService().getProducts()
        call.enqueue(object : Callback<ProductsResponse> {
            override fun onResponse(call: Call<ProductsResponse>, response: Response<ProductsResponse>) {
                if (response.isSuccessful) {
                    val product = response.body()?.products?.find { it.id == productId }
                    if (product != null) {
                       // displayProductDetails(product)
                    }
                } else {
                    Toast.makeText(this@DetailActivity, "Failed to load product details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

   /*fun displayProductDetails(product) {

   }*/
}
