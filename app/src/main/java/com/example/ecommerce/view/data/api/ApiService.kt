package com.example.ecommerce.view.data.api

import com.example.ecommerce.view.data.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}

data class LoginRequest(
    val email: String,
    val password: String
)