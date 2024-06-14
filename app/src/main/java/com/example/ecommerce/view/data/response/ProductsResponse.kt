package com.example.ecommerce.view.data.response

import com.example.ecommerce.view.data.api.ProductsItem

data class ProductsResponse(
	val message: String,
	val status: String,
	val products: List<ProductsItem>
)
