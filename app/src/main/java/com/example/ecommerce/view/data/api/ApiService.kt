package com.example.ecommerce.view.data.api

import com.example.ecommerce.view.data.response.CreatedAt
import com.example.ecommerce.view.data.response.LoginResponse
import com.example.ecommerce.view.data.response.ProductsResponse
import com.example.ecommerce.view.data.response.RecommendResponse
import com.example.ecommerce.view.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("auth/register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @GET("products")
    fun getProducts(): Call<ProductsResponse>

    @GET("recommend")
    fun getRecommend(@Query("input_ids") inputIds: String): Call<List<RecommendResponse>>
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
    val createdAt: CreatedAt,
    val price: Int,
    val name: String,
    val description: String,
    val id: String
)

data class RecommendRequest(
    val inputId : String
)