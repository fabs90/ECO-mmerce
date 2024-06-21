package com.example.ecommerce.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.R
import com.example.ecommerce.databinding.ActivityRecommendBinding
import com.example.ecommerce.view.adapter.RecommendationAdapter
import com.example.ecommerce.view.data.api.ApiConfig
import com.example.ecommerce.view.data.response.RecommendResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendBinding
    private lateinit var recommendAdapter: RecommendationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecommendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initializing RecyclerView
        binding.gridProduct.layoutManager = LinearLayoutManager(this)

        // Sample data, ideally this should come from your API call
        val recommendations = listOf(
            RecommendResponse(
                input_ids = "f449ec65dcbc041b6ae5e6a32717d01b",
                output_score = 0.04885903745889664,
                product_name = "AW Bellies",
                product_url = "http://www.flipkart.com/aw-bellies/p/itmeh4grgfbkexnt?pid=SHOEH4GRSUBJGZXE",
<<<<<<< HEAD
                product_image = "http://img5a.flixcart.com/image/shoe/7/z/z/red-as-454-aw-11-original-imaeebfwsdf6jdf6.jpeg",
                product_price = "Rp 100.000"
=======
>>>>>>> e24250787715668dde188f134a82c0e3fc00d990
            )
        )
        adapter = RecommendationAdapter(recommendations)
        binding.gridProduct.adapter = adapter

        // Example of updating the recommendations
//        getAllRecommendations()
    }
    private fun setupRecyclerView() {
        showLoading(true)
        binding.gridProduct.apply {
            layoutManager = GridLayoutManager(this@RecommendActivity, 2)
            adapter = adapter
        }
    }


//    private fun getAllRecommendations() {
//        val call = ApiConfig.apiService().getRecommend()
//        call.enqueue(object : Callback<List<RecommendResponse>> {
//            override fun onResponse(call: Call<List<RecommendResponse>>, response: Response<List<RecommendResponse>>) {
//                if (response.isSuccessful) {
//                    response.body()?.let { recommendResponseList ->
//                        adapter.updateRecommendationList(recommendResponseList)
//                    }
//                } else {
//                    Toast.makeText(this@RecommendActivity, "Failed to load recommendations", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<List<RecommendResponse>>, t: Throwable) {
//                Toast.makeText(this@RecommendActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//

    private fun showLoading(status: Boolean) {
        binding.progressBar2.visibility = if (status) View.VISIBLE else View.GONE
    }
}
