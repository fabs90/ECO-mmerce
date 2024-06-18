package com.example.ecommerce.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecommerce.databinding.ActivityLoginBinding
import com.example.ecommerce.view.data.api.ApiConfig
import com.example.ecommerce.view.data.api.LoginRequest
import com.example.ecommerce.view.data.response.LoginResponse
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

        binding.backHyperLink.setOnClickListener {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
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
        //Log.e("LoginActivity", "Login call: $call")
        showLoading(true)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {

                        Toast.makeText(
                            this@LoginActivity,
                            loginResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        if (loginResponse.status == "successful") {
                            // Save token if needed and navigate to MainActivity

//                        Log.d("LoginActivity", "Login response received: ${loginResponse.status}")
//                        Log.d("LoginActivity", "Token saved: ${loginResponse.token}")
                            Toast.makeText(
                                this@LoginActivity,
                                "Login Successfully",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            if (loginResponse.status == "successful") {
                                val sharedPreferences =
                                    getSharedPreferences(
                                        MainActivity.SHARED_PREFS,
                                        Context.MODE_PRIVATE
                                    )
                                val editor = sharedPreferences.edit()
                                editor.putString(MainActivity.TOKEN_KEY, loginResponse.token)
                                editor.apply()

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Log.d(
                                    "LoginActivity",
                                    "Login not successful: ${loginResponse.status}"
                                )
                            }
                        } else {
                            //Log.d("LoginActivity", "Empty response body")
                            Toast.makeText(
                                this@LoginActivity,
                                "Empty response body",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        //Log.e("LoginActivity", "Login Response: ${response.message()}")
                        Toast.makeText(
                            this@LoginActivity,
                            "Login failed with response code: ${response.code()} - ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }


            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(
                    this@LoginActivity,
                    "Login failed with error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("LoginActivity", "Login error", t)
            }
        })


    }

    private fun enableEdgeToEdge() {
    }
        // Your implementation for enabling edge-to-edge


        private fun showLoading(status: Boolean) {
            binding.progressBar.visibility = if (status) View.VISIBLE else View.GONE

        }
    }





