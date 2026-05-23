package com.example.e_commerceapp.admin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_commerceapp.SupabaseClient
import com.example.e_commerceapp.admin.EditarUsuarioAdminActivity
import com.example.e_commerceapp.databinding.ActivityEditarCrudUsuarioAdminBinding
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class OpcionesClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarCrudUsuarioAdminBinding
    private var clienteId    = ""
    private var estadoActual = "Activo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarCrudUsuarioAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clienteId    = intent.getStringExtra("id")     ?: ""
        estadoActual = intent.getStringExtra("estado") ?: "Activo"

        val nombre        = intent.getStringExtra("nombre")        ?: ""
        val apellido      = intent.getStringExtra("apellido")      ?: ""
        val email         = intent.getStringExtra("email")         ?: ""
        val telefono      = intent.getStringExtra("telefono")      ?: ""
        val genero        = intent.getStringExtra("genero")        ?: ""
        val dni           = intent.getStringExtra("dni")           ?: ""
        val fechaRegistro = intent.getStringExtra("fechaRegistro") ?: ""
        val direccion     = intent.getStringExtra("direccion")     ?: ""

        binding.tvNombre.text       = "$nombre $apellido"
        binding.tvEmail.text        = email
        binding.tvRol.text          = "Cliente"
        binding.tvEstadoActual.text = estadoActual

        binding.btnBack.setOnClickListener { finish() }

        // Editar → va a EditarUsuarioAdminActivity
        binding.cardEditar.setOnClickListener {
            val intent = Intent(this, EditarUsuarioAdminActivity::class.java)
            intent.putExtra("modo",          "editar")
            intent.putExtra("id",            clienteId)
            intent.putExtra("nombre",        nombre)
            intent.putExtra("apellido",      apellido)
            intent.putExtra("correo",        email)
            intent.putExtra("telefono",      telefono)
            intent.putExtra("genero",        genero)
            intent.putExtra("dni",           dni)
            intent.putExtra("fechaRegistro", fechaRegistro)
            intent.putExtra("estado",        estadoActual)
            intent.putExtra("direccion",     direccion)
            startActivity(intent)
        }

        // Bloquear / Desbloquear
        binding.cardBloquear.setOnClickListener {
            val esBloqueado = estadoActual == "Bloqueado"
            val accion      = if (esBloqueado) "desbloquear" else "bloquear"
            val nuevoEstado = if (esBloqueado) "Activo" else "Bloqueado"

            AlertDialog.Builder(this)
                .setTitle("${accion.replaceFirstChar { it.uppercase() }} cliente")
                .setMessage("¿Seguro que deseas $accion a $nombre $apellido?")
                .setPositiveButton(accion.replaceFirstChar { it.uppercase() }) { _, _ ->
                    lifecycleScope.launch {
                        try {
                            SupabaseClient.client.postgrest["Usuarios"]
                                .update({ set("estado", nuevoEstado) }) {
                                    filter { eq("id", clienteId) }
                                }
                            runOnUiThread {
                                estadoActual               = nuevoEstado
                                binding.tvEstadoActual.text = nuevoEstado
                                binding.tvEstadoActual.setTextColor(
                                    if (nuevoEstado == "Bloqueado")
                                        Color.parseColor("#E24B4A")
                                    else
                                        Color.parseColor("#4CAF50")
                                )
                                Toast.makeText(this@OpcionesClienteActivity,
                                    "Cliente ${nuevoEstado.lowercase()} ✓",
                                    Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            runOnUiThread {
                                Toast.makeText(this@OpcionesClienteActivity,
                                    "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        // Eliminar
        binding.cardEliminar.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar cliente")
                .setMessage("¿Seguro que deseas eliminar a $nombre $apellido? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar") { _, _ ->
                    lifecycleScope.launch {
                        try {
                            SupabaseClient.client.postgrest["Usuarios"]
                                .delete { filter { eq("id", clienteId) } }

                            runOnUiThread {
                                Toast.makeText(this@OpcionesClienteActivity,
                                    "Cliente eliminado ✓", Toast.LENGTH_SHORT).show()
                                // Volver a ClientesActivity limpiando el back stack
                                val intent = Intent(this@OpcionesClienteActivity,
                                    com.example.e_commerceapp.admin.ClientesActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(intent)
                                finish()
                            }
                        } catch (e: Exception) {
                            runOnUiThread {
                                Toast.makeText(this@OpcionesClienteActivity,
                                    "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}