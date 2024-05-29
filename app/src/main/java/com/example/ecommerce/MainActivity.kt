package com.example.ecommerce

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnMoveActivity: Button = findViewById(R.id.btn1)
        val btnMoveActivity2: Button = findViewById(R.id.btn2)
        btnMoveActivity.setOnClickListener(this)
        btnMoveActivity2.setOnClickListener(this)

    }

    override fun onClick(v : View?){
        when (v?.id) {
            R.id.btn1 -> {
                val moveIntent = Intent(this@MainActivity, WelcomeActivity::class.java)
                startActivity(moveIntent)
            }
            R.id.btn2 -> {
                val moveIntent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(moveIntent)
            }
        }
    }

}