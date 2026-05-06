package com.example.e_commerceapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.databinding.ActivityDetalleProductoSellerBinding

class DetalleProductoSellerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleProductoSellerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleProductoSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recibir datos del producto
        val nombre    = intent.getStringExtra("nombre")    ?: "Producto"
        val categoria = intent.getStringExtra("categoria") ?: ""
        val precio    = intent.getStringExtra("precio")    ?: ""
        val stock     = intent.getStringExtra("stock")     ?: ""
        val estado    = intent.getStringExtra("estado")    ?: "Activo"

        // Llenar la UI
        binding.tvNombre.text    = nombre
        binding.tvCategoria.text = "Categoría: $categoria"
        binding.tvPrecio.text    = precio
        binding.tvStock.text     = "$stock unidades"
        binding.tvEstado.text    = estado

        setupBotones()
    }

    private fun setupBotones() {
        binding.btnBack.setOnClickListener { finish() }

        binding.btnEditar.setOnClickListener {
            Toast.makeText(this, "Editar producto", Toast.LENGTH_SHORT).show()
        }

        binding.btnDesactivar.setOnClickListener {
            Toast.makeText(this, "Producto desactivado", Toast.LENGTH_SHORT).show()
        }

        binding.btnEliminar.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Eliminar producto")
                .setMessage("¿Estás seguro de eliminar este producto?")
                .setPositiveButton("Eliminar") { _, _ ->
                    Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}