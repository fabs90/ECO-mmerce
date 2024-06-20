package com.example.ecommerce.view.data.response

import com.google.gson.annotations.SerializedName

data class RecommendResponse(
	val input_ids: String,
	val output_score: Double,
	val product_name: String,
	val product_url: String,
	val product_image: String,
	val product_price: String
)