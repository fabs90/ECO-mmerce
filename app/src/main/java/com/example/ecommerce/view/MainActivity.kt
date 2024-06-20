package com.example.ecommerce.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ActivityMainBinding
import com.example.ecommerce.view.adapter.ProductAdapter
import com.example.ecommerce.view.data.api.ApiConfig
import com.example.ecommerce.view.data.api.ProductsItem
import com.example.ecommerce.view.data.response.ProductsResponse
import com.example.ecommerce.view.detail.DetailActivity
import com.example.ecommerce.view.favorite.FavoriteActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    // creating constant keys for shared preferences.
    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val TOKEN_KEY = "token_key"
        const val USERNAME_KEY = "username"
        const val EMAIL_KEY = "email"
    }


    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var productAdapter: ProductAdapter

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


        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    val query = searchView.text.toString()
                    searchBar.setText(query)
                    searchView.hide()
                    if (query.isNotEmpty()) {
                        val intent = Intent(this@MainActivity, SearchResultActivity::class.java).apply {
                            putExtra("search_query", query)
                        }
                        startActivity(intent)
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

    private fun isUserLoggedIn(): Boolean {
        // Check if the user is logged in via API token
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(TOKEN_KEY, null)

        // Initialize FirebaseAuth
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
        // Logic for setting up the bottom navigation
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
                    val sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
                    val email = sharedPreferences.getString(EMAIL_KEY, "N/A")

                    if (auth.currentUser != null) {
                        // User logged in with Firebase
                        val firebaseUser = auth.currentUser
                        intent.putExtra("username", firebaseUser?.displayName ?: "N/A")
                        intent.putExtra("email", firebaseUser?.email ?: "N/A")
                    } else {
                        // User logged in with API token
                        intent.putExtra("username", email)
                        intent.putExtra("email", email)
                    }
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

    // Agar focus bottom navbar tetap di home
    override fun onResume() {
        super.onResume()
        val navView: BottomNavigationView = binding.bottomNavigation
        navView.selectedItemId = R.id.navigation_home
    }
}