package com.example.e_commerceapp.model

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_commerceapp.SupabaseClient
import com.example.e_commerceapp.admin.ProductosActivity
import com.example.e_commerceapp.databinding.ActivityAgregarProductoFotoBinding
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import java.util.UUID

class AgregarProductoFotosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarProductoFotoBinding
    private var productoId = ""
    private var fotoUri: Uri? = null

    // Lanzador para seleccionar imagen de la galería
    private val seleccionarImagen = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            fotoUri = it
            binding.cardAgregarFoto.setBackgroundColor(
                android.graphics.Color.parseColor("#1A1E90FF")
            )
            Toast.makeText(this, "Foto seleccionada ✓", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarProductoFotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recibir ID del producto creado en el paso 1
        productoId = intent.getStringExtra("producto_id") ?: ""

        setupBotones()
    }

    private fun setupBotones() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnAnterior.setOnClickListener { finish() }

        // Abrir galería al tocar el área de foto
        binding.cardAgregarFoto.setOnClickListener {
            seleccionarImagen.launch("image/*")
        }

        binding.btnSiguiente.setOnClickListener {
            if (fotoUri != null) {
                subirFotoYFinalizar()
            } else {
                // Si no hay foto, finalizar sin foto
                finalizarSinFoto()
            }
        }
    }

    private fun subirFotoYFinalizar() {
        lifecycleScope.launch {
            try {
                android.util.Log.d("FOTO", "Subiendo foto...")

                val uri        = fotoUri ?: return@launch
                val inputStream = contentResolver.openInputStream(uri) ?: return@launch
                val bytes      = inputStream.readBytes()
                inputStream.close()

                // Nombre único para la foto
                val nombreFoto = "productos/${productoId}_${UUID.randomUUID()}.jpg"

                // Subir a Supabase Storage
                SupabaseClient.client.storage
                    .from("productos-fotos")
                    .upload(nombreFoto, bytes)

                // Obtener URL pública de la foto
                val urlFoto = SupabaseClient.client.storage
                    .from("productos-fotos")
                    .publicUrl(nombreFoto)

                android.util.Log.d("FOTO", "URL: $urlFoto")

                // Actualizar el producto con la URL de la foto
                SupabaseClient.client.postgrest["Productos"]
                    .update({ set("foto", urlFoto) }) {
                        filter { eq("id", productoId) }
                    }

                runOnUiThread {
                    Toast.makeText(this@AgregarProductoFotosActivity,
                        "Producto guardado correctamente ✓",
                        Toast.LENGTH_SHORT).show()
                    irAProductos()
                }

            } catch (e: Exception) {
                android.util.Log.e("FOTO", "Error: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@AgregarProductoFotosActivity,
                        "Error al subir foto: ${e.message}",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun finalizarSinFoto() {
        Toast.makeText(this,
            "Producto guardado sin foto ✓",
            Toast.LENGTH_SHORT).show()
        irAProductos()
    }

    private fun irAProductos() {
        finishAffinity()
        startActivity(Intent(this, ProductosActivity::class.java))
    }
}