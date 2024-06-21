package com.example.ecommerce.view.favorite

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ItemFavoriteBinding
import com.example.ecommerce.view.data.api.ProductsItem
import com.example.ecommerce.view.detail.DetailActivity

class FavoriteAdapter(
    private val context: Context,
    private var favoriteList: List<ProductsItem>,
    private val onItemClick: (ProductsItem) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemFavoriteBinding.bind(view)
        val imageView: ImageView = binding.productImage
        val nameTextView: TextView = binding.productName

        fun bind(favorite: ProductsItem) {
            nameTextView.text = favorite.name
            // Optional: priceTextView.text = "Rp.${favorite.price}"

            Log.d("FavoriteAdapter", "Loading image: ${favorite.image}")

            Glide.with(context)
                .load(favorite.image)
                .error(R.drawable.ic_broken) // Add an error placeholder
                .into(imageView)

            binding.root.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_PRODUCT, favorite.id)
                }
                Log.d("FavoriteAdapter", "Navigating to detail with data: $favorite")
                context.startActivity(
                    intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity).toBundle()
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favoriteList[position])
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    fun updateFavoriteList(newFavoriteList: List<ProductsItem>) {
        favoriteList = newFavoriteList
        notifyDataSetChanged()
    }
}
