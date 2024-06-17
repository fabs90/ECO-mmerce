package com.example.ecommerce.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.databinding.ItemFavoriteBinding
import com.example.ecommerce.view.data.api.ProductsItem

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.UserViewHolder>(){

    private val list = ArrayList<ProductsItem>()

    private var onItemClickCallback : OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setList(users: List<ProductsItem>){
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(val binding : ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product : ProductsItem){
            binding.root.setOnClickListener{
                onItemClickCallback?.onItemClicked(product)

            }


            binding.apply {
                Glide.with(itemView.context)
                    .load(product.image)
                    .into(binding.productImage)
                productName.text = product.name
                productPrice.text = product.price
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }

    interface OnItemClickCallback{
        fun onItemClicked(data : ProductsItem){

        }
    }
}