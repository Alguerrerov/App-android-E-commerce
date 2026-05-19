package com.example.e_commerceapp.model

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.admin.ProductosActivity
import com.example.e_commerceapp.databinding.ActivityAgregarProductoFotoBinding

class AgregarProductoFotosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarProductoFotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoFotoBinding.inflate(layoutInflater)
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
            startActivity(Intent(this, ProductosActivity::class.java))
        }
    }
}