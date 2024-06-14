package com.example.ecommerce.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.view.data.api.ProductsItem

class ProductAdapter(private val context: Context, private val productList: List<ProductsItem>) : BaseAdapter() {

    override fun getCount(): Int {
        return productList.size
    }

    override fun getItem(position: Int): Any {
        return productList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        val product = productList[position]

        val imageView: ImageView = view.findViewById(R.id.productImage)
        val nameTextView: TextView = view.findViewById(R.id.productName)
        val priceTextView: TextView = view.findViewById(R.id.productPrice)

        nameTextView.text = product.name
        priceTextView.text = product.price.toString()

        Glide.with(context)
            .load(product.image)
            .into(imageView)

        return view
    }
}