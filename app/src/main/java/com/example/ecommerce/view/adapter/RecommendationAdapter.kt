package com.example.ecommerce.view.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ItemRecommendationBinding
import com.example.ecommerce.view.data.response.RecommendResponse

class RecommendationAdapter(
    private var recommendations: List<RecommendResponse>
) : RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    class RecommendationViewHolder(val binding: ItemRecommendationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding = ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val recommendation = recommendations[position]
        holder.binding.apply {
            productName.text = recommendation.product_name
            productPrice.text = recommendation.product_price
            Glide.with(productImage.context)
                .load(recommendation.product_image)
                .error(R.drawable.ic_broken) // Placeholder for error
                .into(productImage)
        }

        holder.binding.root.setOnClickListener {
            val context = it.context
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recommendation.product_url))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = recommendations.size

    fun updateRecommendationList(newRecommendationList: List<RecommendResponse>) {
        recommendations = newRecommendationList
        notifyDataSetChanged()
    }
}