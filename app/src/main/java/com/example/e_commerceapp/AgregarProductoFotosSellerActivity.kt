package com.example.e_commerceapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.databinding.ActivityAgregarProductoSellerFotoBinding

class AgregarProductoFotosSellerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarProductoSellerFotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoSellerFotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBotones()
    }

    private fun setupBotones() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnAnterior.setOnClickListener { finish() }
        binding.cardAgregarFoto.setOnClickListener {
            Toast.makeText(this, "Seleccionar foto", Toast.LENGTH_SHORT).show()
        }
        binding.btnSiguiente.setOnClickListener {
            Toast.makeText(this, "Producto guardado correctamente ✓",
                Toast.LENGTH_SHORT).show()
            // Volver a la lista de productos
            finishAffinity()
            startActivity(android.content.Intent(this, ProductosSellerActivity::class.java))
        }
    }
}