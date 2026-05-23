package com.example.e_commerceapp.admin

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_commerceapp.SupabaseClient
import com.example.e_commerceapp.databinding.ActivityAgregarProductoBinding
import com.example.e_commerceapp.model.ProductoData
import com.example.e_commerceapp.model.StockTiendasData
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class AgregarProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarProductoBinding
    private val TIENDA_AUTOPARTS_ID = "3b28e9b7-7797-4826-acb3-11ee95692be5"
    private var productoIdGenerado = ""

    // modo editar
    private var modoEditar     = false
    private var productoIdEdit = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        modoEditar     = intent.getStringExtra("modo") == "editar"
        productoIdEdit = intent.getStringExtra("producto_id") ?: ""

        setupCategorias()

        if (modoEditar) {
            binding.etNombre.setText(intent.getStringExtra("nombre"))
            binding.etMarca.setText(intent.getStringExtra("marca"))
            binding.etPrecio.setText(intent.getStringExtra("precio"))
            binding.etStock.setText(intent.getStringExtra("stock"))
            binding.etDescripcion.setText(intent.getStringExtra("descripcion"))
            binding.btnSiguiente.text = "Guardar cambios"
        }

        setupBotones()
    }

    private fun setupCategorias() {
        val categorias = listOf(
            "Filtros de aceite", "Frenos", "Aceites",
            "Baterías", "Suspensión", "Electricidad",
            "Carrocería", "Escape", "Transmisión"
        )
        val adapter = ArrayAdapter(
            this,
            R.layout.simple_dropdown_item_1line, categorias
        )
        binding.spinnerCategoria.setAdapter(adapter)
        binding.spinnerCategoria.threshold = 0  // ← muestra opciones sin escribir nada

        // Si es modo editar, precargar después de setAdapter
        if (modoEditar) {
            binding.spinnerCategoria.setText(intent.getStringExtra("categoria"))
            binding.spinnerCategoria.dismissDropDown()
        }

        // Al hacer foco mostrar todas las opciones
        binding.spinnerCategoria.setOnClickListener {
            binding.spinnerCategoria.showDropDown()
        }
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

            if (modoEditar) {
                actualizarProducto(nombre, categoria, marca, precio, stock, descripcion)
            } else {
                guardarProducto(nombre, categoria, marca, precio, stock, descripcion)
            }
        }
    }

    private fun actualizarProducto(
        nombre: String, categoria: String, marca: String,
        precio: String, stock: String, descripcion: String
    ) {
        lifecycleScope.launch {
            try {
                // Update en Productos
                SupabaseClient.client.postgrest["Productos"]
                    .update({
                        set("nombre",      nombre)
                        set("categoria",   categoria)
                        set("marca",       marca)
                        set("precio",      precio.toDoubleOrNull() ?: 0.0)
                        set("descripcion", descripcion)
                    }) {
                        filter { eq("id", productoIdEdit) }
                    }

                // Update del stock en Stock_tiendas
                SupabaseClient.client.postgrest["Stock_tiendas"]
                    .update({
                        set("stock", stock.toIntOrNull() ?: 0)
                    }) {
                        filter { eq("productos_id", productoIdEdit) }
                    }

                runOnUiThread {
                    Toast.makeText(this@AgregarProductoActivity,
                        "Producto actualizado ✓", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@AgregarProductoActivity,
                        "Error al actualizar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun guardarProducto(
        nombre: String, categoria: String, marca: String,
        precio: String, stock: String, descripcion: String
    ) {
        lifecycleScope.launch {
            try {
                // Paso 1: insertar en Productos
                val producto = SupabaseClient.client
                    .postgrest["Productos"]
                    .insert(
                        ProductoData(
                            nombre = nombre,
                            categoria = categoria,
                            marca = marca,
                            precio = precio.toDoubleOrNull() ?: 0.0,
                            descripcion = descripcion,
                        )
                    ) { select() }
                    .decodeSingle<ProductoData>()

                productoIdGenerado = producto.id
                Log.d("PRODUCTOS", "Producto creado ID: $productoIdGenerado")

                // Paso 2: insertar en stock_tienda
                SupabaseClient.client
                    .postgrest["Stock_tiendas"]
                    .insert(
                        StockTiendasData(
                            productoId = productoIdGenerado,
                            tiendaId = TIENDA_AUTOPARTS_ID,
                            stock = stock.toIntOrNull() ?: 0,
                            estado = "Activo"
                        )
                    )

                Log.d("PRODUCTOS", "Stock asignado a tienda ✓")

                runOnUiThread {
                    Toast.makeText(this@AgregarProductoActivity,
                        "Información guardada ✓ Ahora agrega fotos",
                        Toast.LENGTH_SHORT).show()

                    val intent = Intent(
                        this@AgregarProductoActivity,
                        AgregarProductoFotosActivity::class.java
                    )
                    intent.putExtra("producto_id", productoIdGenerado)
                    intent.putExtra("nombre",      nombre)
                    intent.putExtra("categoria",   categoria)
                    intent.putExtra("marca",       marca)
                    intent.putExtra("precio",      precio)
                    intent.putExtra("stock",       stock)
                    intent.putExtra("descripcion", descripcion)
                    startActivity(intent)
                }

            } catch (e: Exception) {
                Log.e("PRODUCTOS", "Error: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@AgregarProductoActivity,
                        "Error al guardar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}