package com.example.ecommerce.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ActivityLoginBinding
import com.example.ecommerce.databinding.ActivityWelcomeBinding
import com.example.ecommerce.view.data.LoginResponse
import com.example.ecommerce.view.data.api.ApiConfig
import com.example.ecommerce.view.data.api.LoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up button click listener
        binding.button.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password) // Create LoginRequest object
        val call = ApiConfig.apiService().login(loginRequest)
        Log.d("LoginActivity", "Login call: $call")
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        Log.d("LoginActivity", "Login response received: ${loginResponse.status}")
                        Toast.makeText(this@LoginActivity, loginResponse.status, Toast.LENGTH_SHORT).show()
                        if (loginResponse.status == "successful") {
                            Log.d("LoginActivity", "Login successful, navigating to MainActivity")
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.d("LoginActivity", "Login not successful: ${loginResponse.status}")
                        }
                    } else {
                        Log.d("LoginActivity", "Empty response body")
                        Toast.makeText(this@LoginActivity, "Empty response body", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("LoginActivity", "Login Response: ${response.message()}")
                    Toast.makeText(this@LoginActivity, "Login failed with response code: ${response.code()} - ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginActivity", "Login error", t)
                Toast.makeText(this@LoginActivity, "Login failed with error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToMainActivity() {
        Log.d("LoginActivity", "Navigating to MainActivity")
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun enableEdgeToEdge() {
        // Your implementation for enabling edge-to-edge
    }
}
