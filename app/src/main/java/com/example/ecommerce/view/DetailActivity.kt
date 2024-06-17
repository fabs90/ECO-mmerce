package com.example.ecommerce.view

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ActivityDetailBinding
import com.example.ecommerce.view.data.api.ApiConfig
import com.example.ecommerce.view.data.api.ProductsItem
import com.example.ecommerce.view.data.local.FavoriteDatabase
import com.example.ecommerce.view.data.local.FavoriteProduct
import com.example.ecommerce.view.data.local.FavoriteProductDao
import com.example.ecommerce.view.data.response.ProductsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var isFavorite = false
    private lateinit var product: ProductsItem
    private lateinit var binding: ActivityDetailBinding
    private lateinit var favoriteProductDao: FavoriteProductDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteProductDao = FavoriteDatabase.getDatabase(this).favoriteProductDao()
        sharedPreferences = getSharedPreferences("FavoriteItems", MODE_PRIVATE)
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
        setupFavoriteButton()
        //setupAddToCartButton()
    }

    private fun fetchProductDetails(productId: String) {
        val call = ApiConfig.apiService().getProducts()
        call.enqueue(object : Callback<ProductsResponse> {
            override fun onResponse(call: Call<ProductsResponse>, response: Response<ProductsResponse>) {
                if (response.isSuccessful) {
                    val product = response.body()?.products?.find { it.id == productId }
                    if (product != null) {
                       displayProductDetails(product)
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

   private fun displayProductDetails(product: ProductsItem) {
       binding.apply {
           Glide.with(this@DetailActivity)
               .load(product.image)
               .into(imageView3)
           detailTitleTv.text = product.name
          // detailPriceTv.text = product.price.toString()
           descriptionTv.text = product.description
       }
   }
    private fun setupFavoriteButton() {
        val favoriteButton: ImageButton = binding.favoriteBtn
        updateFavoriteButtonState()

        favoriteButton.setOnClickListener {
            isFavorite = !isFavorite
            updateFavoriteButtonState()

            // Save the favorite status to SharedPreferences and Room database
            CoroutineScope(Dispatchers.IO).launch {
                if (isFavorite) {
                    sharedPreferences.edit().putBoolean("favorite_${product.id}", true).apply()
                    favoriteProductDao.addFavoriteProduct(
                        FavoriteProduct(
                            productId = product.id,
                            name = product.name,
                            price = product.price.toDouble(),
                            image = product.image
                        )
                    )
                } else {
                    sharedPreferences.edit().putBoolean("favorite_${product.id}", false).apply()
                    favoriteProductDao.removeFavoriteProduct(product.id)
                }
            }
        }
    }

    private fun updateFavoriteButtonState() {
        val favoriteButton: ImageButton = binding.favoriteBtn
        if (isFavorite) {
            favoriteButton.setBackgroundColor(ContextCompat.getColor(this, R.color.red_main))
        } else {
            favoriteButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        }
    }
    private fun checkIfFavorite(productId: String): Boolean {
        return sharedPreferences.getBoolean("favorite_$productId", false)
    }

    companion object {
        const val EXTRA_PRODUCT = "product_id"
   }
}
