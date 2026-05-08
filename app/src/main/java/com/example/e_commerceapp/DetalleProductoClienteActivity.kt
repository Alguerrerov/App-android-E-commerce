package com.example.e_commerceapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.databinding.ActivityDetalleProductoClienteBinding

class DetalleProductoClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleProductoClienteBinding
    private var cantidad = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleProductoClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recibir datos
        val nombre = intent.getStringExtra("nombre") ?: "Producto"
        val precio = intent.getStringExtra("precio") ?: "$0"
        val rating = intent.getStringExtra("rating") ?: "4.5"
        val stock  = intent.getBooleanExtra("enStock", true)

        // Llenar UI
        binding.tvNombre.text = nombre
        binding.tvPrecio.text = precio
        binding.tvRating.text = " $rating (128)"
        binding.tvStock.text  = if (stock) "En stock" else "Agotado"
        binding.tvStock.setTextColor(
            android.graphics.Color.parseColor(if (stock) "#4CAF50" else "#E24B4A")
        )
        binding.tvCantidad.text = cantidad.toString()

        setupBotones()
    }

    private fun setupBotones() {
        binding.btnBack.setOnClickListener { finish() }

        binding.btnMenos.setOnClickListener {
            if (cantidad > 1) {
                cantidad--
                binding.tvCantidad.text = cantidad.toString()
            }
        }

        binding.btnMas.setOnClickListener {
            cantidad++
            binding.tvCantidad.text = cantidad.toString()
        }

        binding.btnFavorito.setOnClickListener {
            Toast.makeText(this, "Agregado a favoritos ♥", Toast.LENGTH_SHORT).show()
        }

        binding.btnCompartir.setOnClickListener {
            Toast.makeText(this, "Compartir producto", Toast.LENGTH_SHORT).show()
        }

        binding.btnAgregarCarrito.setOnClickListener {
            Toast.makeText(this,
                "$cantidad unidad(es) agregada(s) al carrito ✓",
                Toast.LENGTH_SHORT).show()
        }

        binding.btnPagar.setOnClickListener {
            Toast.makeText(this, "Procesando pago...", Toast.LENGTH_SHORT).show()
        }
    }
}