package com.example.ecommerce.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
// Nanti tambahin ini setelah AppCOmpatActivity()
    // , View.OnClickListener
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        val btnMoveActivity: Button = findViewById(R.id.btn1)
//        val btnMoveActivity2: Button = findViewById(R.id.btn2)
//        btnMoveActivity.setOnClickListener(this)
//        btnMoveActivity2.setOnClickListener(this)

        /*
           * Inititate Firebase
           * */
        auth = Firebase.auth

        /*
           * Check if user not login then redirect to LoginActivity
           * */
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            // Not signed in, launch the Login activity
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
            return
        }

        /*
            Set the bottom navigation
         */
        val navView : BottomNavigationView = binding.bottomNavigation
        navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_favorite -> {
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

//    override fun onClick(v : View?){
//        when (v?.id) {
//            R.id.btn1 -> {
//                val moveIntent = Intent(this@MainActivity, WelcomeActivity::class.java)
//                startActivity(moveIntent)
//            }
//            R.id.btn2 -> {
//                val moveIntent = Intent(this@MainActivity, LoginActivity::class.java)
//                startActivity(moveIntent)
//            }
//        }
//    }



}