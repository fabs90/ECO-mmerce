package com.example.ecommerce.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ActivityLoginBinding
import com.example.ecommerce.databinding.ActivityRegisterBinding
import com.example.ecommerce.view.data.api.ApiConfig
import com.example.ecommerce.view.data.api.ApiService
import com.example.ecommerce.view.data.api.RegisterRequest
import com.example.ecommerce.view.data.response.RegisterResponse
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize binding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            button.setOnClickListener {
                val name = nameEditText.text.toString()
                val email = emailEditText.text.toString()
                val phone = phoneEditText.text.toString()
                val password = passwordEditText.text.toString()

                if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()){
                    showToast("Please fill all fields")
                    return@setOnClickListener
                }

                registerUser(name, email, phone, password)
            }

            loginHyperLink.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }
    }

    private fun registerUser(name: String, email: String, phone: String, password: String) {
        val registerRequest = RegisterRequest(name, email, phone, password)
        val call = ApiConfig.apiService().register(registerRequest)
        Log.e("RegisterActivity", "Register call: $call")
        showLoading(true)
        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.e("RegisterActivity", "Register response: $it")
                        showDialog()
                    } ?: run {
                        Log.e("RegisterActivity", "Register response body is null")
                        showToast("Empty response body")
                    }
                } else {
                    Log.e("RegisterActivity", "Error: ${response.code()}")
                    showToast("Register failed with response code: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                showLoading(false)
                showToast("Register failed with error: ${t.message}")
                Log.e("RegisterActivity", "Register error", t)
            }
        })
    }

    private fun showDialog() {
        AlertDialog.Builder(this)
            .setMessage("Successful register, login first!😁")
            .setTitle("Notification")
            .setPositiveButton("Ok") { _, _ ->
                startActivity(Intent(this, LoginActivity::class.java))
            }
            .create()
            .show()
    }

    private fun showLoading(status: Boolean) {
        binding.progressBarRegister.visibility = if (status) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}