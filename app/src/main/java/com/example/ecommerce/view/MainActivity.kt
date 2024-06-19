package com.example.ecommerce.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ActivityMainBinding
import com.example.ecommerce.view.adapter.ProductAdapter
import com.example.ecommerce.view.data.api.ApiConfig
import com.example.ecommerce.view.data.api.ProductsItem
import com.example.ecommerce.view.data.response.ProductsResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import org.tensorflow.lite.Interpreter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class MainActivity : AppCompatActivity() {

    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val TOKEN_KEY = "token_key"
        const val OUTPUT_SIZE = 50 // Adjust according to your model's output size
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var productAdapter: ProductAdapter
    private lateinit var tfliteInterpreter: Interpreter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")

        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load TFLite model
        tfliteInterpreter = Interpreter(loadModelFile(this, "model_with_metadata.tflite"))

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
                val query = searchView.text.toString()
                searchBar.setText(query)
                searchView.hide()
                if (query.isNotEmpty()) {
                    val inputIds = getInputIdsFromQuery(query)
                    if (inputIds.isNotEmpty()) {
                        val inputIdsFloat = inputIds.map { it.toFloat() }.toFloatArray() // Convert to FloatArray
                        // Prepare the input tensor as a 2D array [1, inputIdsFloat.size]
                        val inputTensor = arrayOf(inputIdsFloat)
                        val output = FloatArray(OUTPUT_SIZE) // Update: Initialize output as a 1D FloatArray

                        Log.e("MainActivity", "Input id Float: ${inputIdsFloat.contentToString()}")
                        Log.e("MainActivity", "Output shape before inference: ${output.contentToString()}")

                        try {
                            // Run the inference
                            tfliteInterpreter.run(inputTensor, output) // Update: Pass output directly

                            // Log the output tensor shape
                            Log.d("MainActivity", "Output tensor shape: ${output.size}")

                            if (output.isNotEmpty()) {
                                displayRecommendations(output)
                            } else {
                                Toast.makeText(this@MainActivity, "No recommendations available", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: IllegalArgumentException) {
                            Log.e("MainActivity", "Error during model inference: ${e.message}")
                            Toast.makeText(this@MainActivity, "Model inference failed", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Input data is empty after preprocessing", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Search query is empty", Toast.LENGTH_SHORT).show()
                }
                false
            }
        }

        // Check login status and handle redirection
        if (isUserLoggedIn()) {
            setupNavigation()
            setupRecyclerView()
            getAllProducts()
        }
    }

    private fun getInputIdsFromQuery(query: String): IntArray {
        Log.e("MainActivity", "Original query: $query")
        val inputIds = query.split(" ").map { it.hashCode() }.toIntArray()
        Log.e("MainActivity", "Processed input IDs: ${inputIds.joinToString(", ")}")
        return inputIds
    }

    private fun displayRecommendations(output: FloatArray) {
        // Implement logic to display recommendations based on model output
        Log.e("MainActivity", "Recommendations: ${output.contentToString()}")
        val recommendedProducts = getRecommendedProducts(output)
        productAdapter.updateProductList(recommendedProducts)
    }

    private fun getRecommendedProducts(output: FloatArray): List<ProductsItem> {
        // Implement logic to get products based on recommendation scores
        // This is a simple example; adjust to your needs
        return listOf() // Replace with actual products based on output
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(TOKEN_KEY, null)
        auth = FirebaseAuth.getInstance()
        val firebaseUser = auth.currentUser

        return when {
            token != null -> {
                Log.d("MainActivity", "User logged in with API token")
                true
            }
            firebaseUser != null -> {
                Log.d("MainActivity", "User logged in with Firebase: ${firebaseUser.email}")
                true
            }
            else -> {
                Log.d("MainActivity", "User not logged in, redirecting to LoginActivity")
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                false
            }
        }
    }

    private fun setupNavigation() {
        val navView: BottomNavigationView = binding.bottomNavigation
        navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    Log.d("MainActivity", "Home selected")
                    true
                }
                R.id.navigation_profile -> {
                    Log.d("MainActivity", "Profile selected")
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_favorite -> {
                    Log.d("MainActivity", "Favorite selected")
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

    private fun setupRecyclerView() {
        showLoading(true)
        productAdapter = ProductAdapter(this, listOf()) { product ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("product_id", product.id)
            startActivity(intent)
        }
        binding.gridProduct.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = productAdapter
        }
    }

    private fun getAllProducts() {
        val call = ApiConfig.apiService().getProducts()
        call.enqueue(object : Callback<ProductsResponse> {
            override fun onResponse(call: Call<ProductsResponse>, response: Response<ProductsResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val products = response.body()?.products ?: emptyList()
                    productAdapter.updateProductList(products)
                } else {
                    Toast.makeText(this@MainActivity, "Failed to load products", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoading(status: Boolean) {
        binding.progressBar2.visibility = if (status) View.VISIBLE else View.GONE
    }

    fun loadModelFile(context: Context, modelName: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
}
