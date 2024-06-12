package com.example.ecommerce.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ActivityLoginBinding
import com.example.ecommerce.view.data.api.ApiConfig
import com.example.ecommerce.view.data.api.ApiService
import com.example.ecommerce.view.data.api.RegisterRequest
import com.example.ecommerce.view.data.response.RegisterResponse
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log

class RegisterActivity : AppCompatActivity() {
    private lateinit var nameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var registerButton: Button
    private lateinit var loginHyperlink: TextView

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.button)
        loginHyperlink = findViewById(R.id.loginHyperLink)


        // Initialize binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val password = passwordEditText.text.toString()

            registerUser(name, email, phone, password)
        }
        val textHyperLink: TextView = findViewById(R.id.loginHyperLink)
        textHyperLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
    private fun registerUser(name: String, email: String, phone: String, password: String) {
        val registerRequest = RegisterRequest(name, email, phone, password)
        val call = ApiConfig.apiService().register(registerRequest)
        Log.e("RegisterActivity", "Register call: $call")

        call.enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: retrofit2.Call<RegisterResponse>,
                response: retrofit2.Response<RegisterResponse>
            )
            {}

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }
}