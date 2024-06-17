package com.example.ecommerce.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecommerce.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class FavoriteActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

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

    }

    }

    private fun isUserLoggedIn(): Boolean {
        // Check if the user is logged in via API token
        val sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(MainActivity.TOKEN_KEY, null)

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
}