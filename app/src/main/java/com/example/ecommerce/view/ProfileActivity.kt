package com.example.ecommerce.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecommerce.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private  var isFirebase : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Check login status and handle redirection
        if (isUserLoggedIn()) {
            updateProfileDetails()
            binding.btnSignOut.setOnClickListener {
                // Handle sign out logic here
                if(isFirebase == true){
                    signOutFirebase()
                } else {
                    signOut()
                }
            }
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
                Log.d("ProfileActivity", "User logged in with API token")
                isFirebase = false
                true
            }
            firebaseUser != null -> {
                Log.d("ProfileActivity", "User logged in with Firebase: ${firebaseUser.email}")
                isFirebase = true
                true
            }
            else -> {
                Log.d("ProfileActivity", "User not logged in, redirecting to LoginActivity")
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                false
            }
        }
    }

    private fun updateProfileDetails() {
        if (isFirebase) {
            val firebaseUser = auth.currentUser
            firebaseUser?.let {
                binding.textViewProfilename.text = it.displayName ?: "N/A"
                binding.textViewProfileEmail.text = it.email ?: "N/A"
            }
        } else {
            val sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE)
            val email = sharedPreferences.getString(MainActivity.EMAIL_KEY, "N/A")

            binding.textViewProfilename.text = email
            binding.textViewProfileEmail.text = email
        }
    }

    // Sign out
    private fun signOut() {
        val sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(MainActivity.TOKEN_KEY, null)

        if (token != null) {
            // Logout for API-based login
            sharedPreferences.edit().remove(MainActivity.TOKEN_KEY).apply()
            Log.d("ProfileActivity", "User logged out from API-based login")
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            signOutFirebase()
        }

    }

    private fun signOutFirebase() {
        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Sign out the current user
        try {
            auth.signOut()
            Log.d("ProfileActivity", "Firebase user signed out successfully")
            // Redirect to WelcomeActivity
            val intent = Intent(this@ProfileActivity, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Log.e("ProfileActivity", "Error signing out Firebase user: ${e.message}")
        }

    }


}
