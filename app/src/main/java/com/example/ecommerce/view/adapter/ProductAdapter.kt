package com.example.ecommerce.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.view.data.api.ProductsItem

class ProductAdapter(
    private val context: Context,
    private var productList: List<ProductsItem>,
    private val onItemClick: (ProductsItem) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.productImage)
        val nameTextView: TextView = view.findViewById(R.id.productName)
        val priceTextView: TextView = view.findViewById(R.id.productPrice)

        init {
            view.setOnClickListener {
                onItemClick(productList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.nameTextView.text = product.name
        holder.priceTextView.text = product.price.toString()

        Log.d("ProductAdapter", "Loading image: ${product.image}")
        Glide.with(context)
            .load(product.image)
            .error(R.drawable.ic_broken) // Add an error placeholder
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun updateProductList(newProductList: List<ProductsItem>) {
        productList = newProductList
        notifyDataSetChanged()
    }
}