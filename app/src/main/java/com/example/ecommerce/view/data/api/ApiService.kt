package com.example.ecommerce.view.data.api

import com.example.ecommerce.view.data.response.LoginResponse
import com.example.ecommerce.view.data.response.ProductsResponse
import com.example.ecommerce.view.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("auth/register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @GET("products")
    fun getProducts(): Call<ProductsResponse>
}

data class LoginRequest(
    val email: String,
    val password: String
)
data class RegisterRequest(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val password: String

)
data class ProductsItem(
    val image: String,
    val createdAt: String,
    val price: String,
    val name: String,
    val description: String,
    val id: String
)