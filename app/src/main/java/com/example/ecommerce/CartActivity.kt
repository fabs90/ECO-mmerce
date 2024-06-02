package com.example.ecommerce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import java.text.NumberFormat
import java.util.Locale

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var totalPriceTextView: TextView
    private lateinit var cartAdapter: CartAdapter
    private var cartItems = mutableListOf(
        CartItem("Item 1", 10000.0, 1, R.drawable.img_example),
        CartItem("Item 2", 20000.0, 2, R.drawable.img_example),
        CartItem("Item 3", 30000.0, 3, R.drawable.img_example)
    ) // Contoh data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)



        recyclerView = findViewById(R.id.recyclerViewCart)

        cartAdapter = CartAdapter(cartItems, ::updateTotalPrice)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = cartAdapter

        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        val totalPrice = cartItems.sumOf { it.price * it.quantity }
        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
//        totalPriceTextView.text = getString(R.string.total_price, formatRupiah.format(totalPrice))
    }
}

data class CartItem(val name: String, val price: Double, var quantity: Int, val imageResId: Int)

class CartAdapter(
    private val items: MutableList<CartItem>,
    private val updateTotalPrice: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.buttonPlus.setOnClickListener {
            item.quantity++
            notifyItemChanged(position)
            updateTotalPrice()
        }

        holder.buttonMinus.setOnClickListener {
            if (item.quantity > 1) {
                item.quantity--
                notifyItemChanged(position)
                updateTotalPrice()
            }
        }

        holder.buttonDelete.setOnClickListener {
            items.removeAt(position)
            notifyItemRemoved(position)
            updateTotalPrice()
        }
    }

    override fun getItemCount() = items.size

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemNameTextView: TextView = itemView.findViewById(R.id.textViewItemName)
        private val itemPriceTextView: TextView = itemView.findViewById(R.id.textViewItemPrice)
        private val itemImageView: CircleImageView = itemView.findViewById(R.id.imageViewItem)
        private val itemQuantityTextView: TextView = itemView.findViewById(R.id.textViewItemQuantity)
        val buttonPlus: Button = itemView.findViewById(R.id.buttonPlus)
        val buttonMinus: Button = itemView.findViewById(R.id.buttonMinus)
        val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)

        fun bind(item: CartItem) {
            itemNameTextView.text = item.name
            val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            itemPriceTextView.text = formatRupiah.format(item.price)
            itemQuantityTextView.text = item.quantity.toString()
            itemImageView.setImageResource(item.imageResId)
        }
    }
}