package com.example.ecommerce.view.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("status")
	val status: String
)