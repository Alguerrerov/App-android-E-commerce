package com.example.e_commerceapp.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_commerceapp.SupabaseClient
import com.example.e_commerceapp.databinding.ActivityDetalleProductoBinding
import com.example.e_commerceapp.model.AgregarProductoActivity
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class DetalleProductoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleProductoBinding
    private var productoId        = ""
    private var estadoActual      = "Activo"
    private var nombreActual      = ""
    private var categoriaActual   = ""
    private var marcaActual       = ""
    private var precioActual      = ""
    private var stockActual       = ""
    private var descripcionActual = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Guardar todos los datos en variables de clase
        productoId        = intent.getStringExtra("id")          ?: ""
        estadoActual      = intent.getStringExtra("estado")      ?: "Activo"
        nombreActual      = intent.getStringExtra("nombre")      ?: ""
        categoriaActual   = intent.getStringExtra("categoria")   ?: ""
        marcaActual       = intent.getStringExtra("marca")       ?: ""
        precioActual      = intent.getStringExtra("precio")      ?: "" // viene como "S/ 45.0"
        stockActual       = intent.getStringExtra("stock")       ?: ""
        descripcionActual = intent.getStringExtra("descripcion") ?: ""

        // Llenar UI
        binding.tvNombre.text      = nombreActual
        binding.tvCategoria.text   = "Categoría: $categoriaActual"
        binding.tvPrecio.text      = precioActual
        binding.tvStock.text       = "$stockActual unidades"
        binding.tvEstado.text      = estadoActual
        binding.tvDescripcion.text = descripcionActual
        binding.tvMarca.text       = marcaActual
        binding.tvCodigo.text      = intent.getStringExtra("codigo") ?: ""
        binding.tvVendidos.text    = "0"
        binding.tvVistas.text      = "0"

        setupBotones()
    }

    private val editarLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // El producto fue editado, cerrar esta pantalla
            // para que ProductosActivity recargue la lista
            finish()
        }
    }



    private fun setupBotones() {
        binding.btnBack.setOnClickListener { finish() }

        // Editar
        binding.btnEditar.setOnClickListener {
            val intent = Intent(this, AgregarProductoActivity::class.java)
            intent.putExtra("modo",        "editar")
            intent.putExtra("producto_id", productoId)
            intent.putExtra("nombre",      nombreActual)
            intent.putExtra("categoria",   categoriaActual)
            intent.putExtra("marca",       marcaActual)
            intent.putExtra("precio",      precioActual.replace("S/ ", "").trim())
            intent.putExtra("stock",       stockActual)
            intent.putExtra("descripcion", descripcionActual)
            editarLauncher.launch(intent)
        }

        // Desactivar / Activar
        binding.btnDesactivar.setOnClickListener {
            val esActivo    = estadoActual == "Activo"
            val accion      = if (esActivo) "desactivar" else "activar"
            val nuevoEstado = if (esActivo) "Inactivo" else "Activo"

            AlertDialog.Builder(this)
                .setTitle("${accion.replaceFirstChar { it.uppercase() }} producto")
                .setMessage("¿Seguro que deseas $accion este producto?")
                .setPositiveButton(accion.replaceFirstChar { it.uppercase() }) { _, _ ->
                    lifecycleScope.launch {
                        try {
                            // Actualizar estado en Stock_tiendas, no en Productos
                            SupabaseClient.client.postgrest["Stock_tiendas"]
                                .update({
                                    set("estado", nuevoEstado)
                                }) {
                                    filter { eq("productos_id", productoId) }
                                }

                            runOnUiThread {
                                estadoActual               = nuevoEstado
                                binding.tvEstado.text      = nuevoEstado
                                binding.btnDesactivar.text =
                                    if (nuevoEstado == "Activo") "Desactivar" else "Activar"
                                Toast.makeText(this@DetalleProductoActivity,
                                    "Estado actualizado ✓", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            runOnUiThread {
                                Toast.makeText(this@DetalleProductoActivity,
                                    "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        // Eliminar
        binding.btnEliminar.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar producto")
                .setMessage("¿Estás seguro? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar") { _, _ ->
                    lifecycleScope.launch {
                        try {
                            SupabaseClient.client.postgrest["Productos"]
                                .delete {
                                    filter { eq("id", productoId) }
                                }
                            runOnUiThread {
                                Toast.makeText(this@DetalleProductoActivity,
                                    "Producto eliminado ✓", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        } catch (e: Exception) {
                            runOnUiThread {
                                Toast.makeText(this@DetalleProductoActivity,
                                    "Error al eliminar: ${e.message}",
                                    Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

    }
}