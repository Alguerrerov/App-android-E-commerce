package com.example.e_commerceapp.seller

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.databinding.ActivityActivarVendedorBinding

class ActivarVendedorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityActivarVendedorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActivarVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.btnActivar.setOnClickListener {
            Toast.makeText(this, "Modo vendedor activado", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SellersMainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}