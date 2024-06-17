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
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ActivityDetailBinding
import com.example.ecommerce.view.data.api.ApiConfig
import com.example.ecommerce.view.data.api.ProductsItem
import com.example.ecommerce.view.data.local.FavoriteDatabase
import com.example.ecommerce.view.data.local.FavoriteProduct
import com.example.ecommerce.view.data.local.FavoriteProductDao
import com.example.ecommerce.view.data.response.ProductsResponse
import com.example.ecommerce.view.model.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private var isFavorite = false

    private lateinit var binding: ActivityDetailBinding

    private lateinit var viewModel: ViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_PRODUCT)
        val name = intent.getStringExtra(EXTRA_NAME)
        val price = intent.getStringExtra(EXTRA_PRICE)
        val image = intent.getStringExtra(EXTRA_IMAGE)

      //  favoriteProductDao = FavoriteDatabase.getDatabase(this).favoriteProductDao()
       // sharedPreferences = getSharedPreferences("FavoriteItems", MODE_PRIVATE)
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

        //setupFavoriteButton()
        //setupAddToCartButton()
        viewModel.viewModelScope.launch {
            val count = id?.let { viewModel.checkFavorite(it) }
            if (count != null){
                isFavorite = count > 0
                updateFavoriteButton()
            }
        }
        binding.favoriteBtn.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) {
                viewModel.addToFavorite(name.orEmpty() ,id.orEmpty(), price.toString(), image.orEmpty())
            }else{
                id?.let { it1 -> viewModel.removeFromFavorite(it1) }
            }
            updateFavoriteButton()
        }
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
    private fun updateFavoriteButton() {
        binding.favoriteBtn.setImageResource(
            if (isFavorite) R.drawable.ic_favorite_red
            else R.drawable.ic_favorite
        )
    }
  /*  private fun setupFavoriteButton() {
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
    }*/

    companion object {
        const val EXTRA_PRODUCT = "product_id"
        const val EXTRA_NAME = "product_name"
        const val EXTRA_PRICE = "product_price"
        const val EXTRA_IMAGE = "product_image"
   }
}
