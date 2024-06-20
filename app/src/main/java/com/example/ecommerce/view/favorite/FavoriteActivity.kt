package com.example.ecommerce.view.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ActivityFavoriteBinding
import com.example.ecommerce.view.LoginActivity
import com.example.ecommerce.view.MainActivity
import com.example.ecommerce.view.adapter.ProductAdapter
import com.example.ecommerce.view.data.local.FavoriteProductDao
import com.google.firebase.auth.FirebaseAuth

class FavoriteActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var favoriteProductDao: FavoriteProductDao
    private lateinit var favoriteAdapter: ProductAdapter
    private lateinit var binding: ActivityFavoriteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorite)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    if(isUserLoggedIn()) {
        //loadFavoriteItems()
        //setupRecyclerView()
    }

    }
    /*private fun setupRecyclerView() {
        binding.recyclerViewFavorite.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            favoriteAdapter = ProductAdapter(this@FavoriteActivity, emptyList()) // Initialize with empty list
            adapter = favoriteAdapter
        }
    }

    private fun loadFavoriteItems() {
        CoroutineScope(Dispatchers.IO).launch {
            val favoriteItems = favoriteProductDao.getFavoriteProducts()
            runOnUiThread {
                favoriteAdapter.updateProductList(favoriteItems)
            }
        }
    }*/

    private fun isUserLoggedIn(): Boolean {
        // Check if the user is logged in via API token
        val sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(MainActivity.TOKEN_KEY, null)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()
        val firebaseUser = auth.currentUser

        return when {
            token != null -> {
                Log.d("com.example.ecommerce.view.MainActivity", "User logged in with API token")
                true
            }
            firebaseUser != null -> {
                Log.d("com.example.ecommerce.view.MainActivity", "User logged in with Firebase: ${firebaseUser.email}")
                true
            }
            else -> {
                Log.d("com.example.ecommerce.view.MainActivity", "User not logged in, redirecting to LoginActivity")
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                false
            }
        }
    }
}