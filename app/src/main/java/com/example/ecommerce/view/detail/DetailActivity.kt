package com.example.ecommerce.view.detail

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ActivityDetailBinding
import com.example.ecommerce.view.data.api.ApiConfig
import com.example.ecommerce.view.data.api.ProductsItem
import com.example.ecommerce.view.data.response.ProductsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var isChecked = false
    private var currentProduct: ProductsItem? = null

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

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        val productId = intent.getStringExtra("product_id")
        Log.e(this.toString(),"Product Id : $productId")
        if (productId != null) {
            fetchProductDetails(productId)
        }

        // check if product is loved
        viewModel.isFavorite.observe(this, Observer { favorite ->
            isChecked = favorite
            updateFavoriteIcon()
        })

        binding.favoriteBtn.setOnClickListener {
            isChecked = !isChecked
            currentProduct?.let { product ->
                if (isChecked) {
                    viewModel.addToFavorite(product.image, product.name, product.description)
                    Toast.makeText(this, "${product.name} Added to favorites", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.removeFromFavorite(product.name)
                    Toast.makeText(this, "${product.name} Removed from favorites", Toast.LENGTH_SHORT).show()
                }
                updateFavoriteIcon()
            }
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
                        currentProduct = product
                        viewModel.checkProduct(product.name)
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
            descriptionTv.text = product.description
        }
    }

    private fun updateFavoriteIcon() {
        binding.favoriteBtn.setImageResource(if (isChecked) R.drawable.ic_heart_red else R.drawable.ic_favorite)
    }

    companion object {
        const val EXTRA_PRODUCT = "product_id"
    }
}
