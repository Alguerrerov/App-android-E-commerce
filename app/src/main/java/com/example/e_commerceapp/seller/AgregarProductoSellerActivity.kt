package com.example.e_commerceapp.seller

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.databinding.ActivityAgregarProductoSellerBinding

class AgregarProductoSellerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarProductoSellerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupCategorias()
        setupBotones()
    }

    private fun setupCategorias() {
        val categorias = listOf(
            "Filtros de aceite", "Frenos", "Aceites",
            "Baterías", "Suspensión", "Electricidad",
            "Carrocería", "Escape", "Transmisión"
        )
        binding.spinnerCategoria.setAdapter(
            ArrayAdapter(this, R.layout.simple_dropdown_item_1line, categorias)
        )
    }

    private fun setupBotones() {
        binding.btnBack.setOnClickListener { finish() }

        binding.btnSiguiente.setOnClickListener {
            val nombre      = binding.etNombre.text.toString().trim()
            val categoria   = binding.spinnerCategoria.text.toString().trim()
            val marca       = binding.etMarca.text.toString().trim()
            val precio      = binding.etPrecio.text.toString().trim()
            val stock       = binding.etStock.text.toString().trim()
            val descripcion = binding.etDescripcion.text.toString().trim()

            if (nombre.isEmpty() || categoria.isEmpty() ||
                marca.isEmpty() || precio.isEmpty() || stock.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos obligatorios",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Pasar datos al paso 2 (fotos)
            val intent = Intent(this, AgregarProductoFotosSellerActivity::class.java)
            intent.putExtra("nombre",      nombre)
            intent.putExtra("categoria",   categoria)
            intent.putExtra("marca",       marca)
            intent.putExtra("precio",      precio)
            intent.putExtra("stock",       stock)
            intent.putExtra("descripcion", descripcion)
            startActivity(intent)
        }
    }
}