package com.example.ecommerce.view.favorite;

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce.R
import com.example.ecommerce.data.FavoriteProduct
import com.example.ecommerce.databinding.ActivityFavoriteBinding
import com.example.ecommerce.view.LoginActivity
import com.example.ecommerce.view.MainActivity
import com.example.ecommerce.view.MainActivity.Companion.EMAIL_KEY
import com.example.ecommerce.view.MainActivity.Companion.SHARED_PREFS
import com.example.ecommerce.view.ProfileActivity
import com.example.ecommerce.view.adapter.ProductAdapter
import com.example.ecommerce.view.data.api.ProductsItem
import com.example.ecommerce.view.data.response.CreatedAt
import com.example.ecommerce.view.detail.DetailActivity
import com.example.ecommerce.view.favorite.FavoriteViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class FavoriteActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (isUserLoggedIn()) {
            setupNavigation()
            setupRecyclerView()
            setupViewModel()
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(MainActivity.TOKEN_KEY, null)
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
                    startActivity(Intent(this@FavoriteActivity, MainActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    val sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE)
                    val email = sharedPreferences.getString(MainActivity.EMAIL_KEY, "N/A")

                    if (auth.currentUser != null) {
                        val firebaseUser = auth.currentUser
                        intent.putExtra("username", firebaseUser?.displayName ?: "N/A")
                        intent.putExtra("email", firebaseUser?.email ?: "N/A")
                    } else {
                        intent.putExtra("username", email)
                        intent.putExtra("email", email)
                    }
                    startActivity(intent)
                    true
                }
                R.id.navigation_favorite -> false
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        showLoading(true)
        favoriteAdapter = FavoriteAdapter(this, listOf())
        binding.recyclerViewFavorite.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            adapter = favoriteAdapter
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        viewModel.getFavoriteUser()?.observe(this, Observer { favoriteProducts ->
            favoriteProducts?.let {
                val products = it.map { favoriteProduct ->
                    ProductsItem(
                        id = favoriteProduct.id.toString(),
                        name = favoriteProduct.name,
                        image = favoriteProduct.image,
                        description = favoriteProduct.description,
                        price = 0,
                        createdAt = CreatedAt(0, 0)
                    )
                }
                favoriteAdapter.updateFavoriteList(products)
                showLoading(false)
            }
        })
    }

    private fun showLoading(status: Boolean) {
        binding.progressBar3.visibility = if (status) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        val navView: BottomNavigationView = binding.bottomNavigation
        navView.selectedItemId = R.id.navigation_favorite
    }
}