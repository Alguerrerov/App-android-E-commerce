package com.example.e_commerceapp.admin

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_commerceapp.databinding.ActivityEditarUsuarioAdminBinding
import com.example.e_commerceapp.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class EditarUsuarioAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarUsuarioAdminBinding
    private var usuarioId  = ""
    private var modoEditar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarUsuarioAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        modoEditar = intent.getStringExtra("modo") == "editar"
        usuarioId  = intent.getStringExtra("id") ?: ""

        setupDropdowns(
            intent.getStringExtra("estado") ?: "Activo",
            intent.getStringExtra("rol")    ?: "Cliente"
        )

        if (modoEditar) {
            binding.etNombreCompleto.setText(intent.getStringExtra("nombre"))
            binding.etNombreUsuario.setText(intent.getStringExtra("apellido"))
            binding.etCorreo.setText(intent.getStringExtra("correo"))
            binding.btnActualizar.text = "Guardar cambios"
        }

        setupBotones()
    }

    private fun setupDropdowns(estadoActual: String, rolActual: String) {
        val estados = listOf("Activo", "Inactivo", "Bloqueado")
        binding.spinnerEstado.threshold = 0
        binding.spinnerEstado.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, estados)
        )
        binding.spinnerEstado.setText(estadoActual, false)
        binding.spinnerEstado.setOnClickListener { binding.spinnerEstado.showDropDown() }

        val roles = listOf("Cliente", "Vendedor", "Admin")
        binding.spinnerRol.threshold = 0
        binding.spinnerRol.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        )
        binding.spinnerRol.setText(rolActual, false)
        binding.spinnerRol.setOnClickListener { binding.spinnerRol.showDropDown() }
    }

    private fun setupBotones() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnCancelar.setOnClickListener { finish() }

        binding.btnActualizar.setOnClickListener {
            val nombre   = binding.etNombreCompleto.text.toString().trim()
            val apellido = binding.etNombreUsuario.text.toString().trim()
            val correo   = binding.etCorreo.text.toString().trim()
            val estado   = binding.spinnerEstado.text.toString().trim()

            if (nombre.isEmpty() || correo.isEmpty()) {
                Toast.makeText(this, "Completa los campos obligatorios",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            actualizarUsuario(nombre, apellido, correo, estado)
        }
    }

    private fun actualizarUsuario(
        nombre: String, apellido: String,
        correo: String, estado: String
    ) {
        lifecycleScope.launch {
            try {
                SupabaseClient.client.postgrest["Usuarios"]
                    .update({
                        set("nombre",   nombre)
                        set("apellido", apellido)
                        set("correo",   correo)
                        set("estado",   estado)
                    }) {
                        filter { eq("id", usuarioId) }
                    }

                runOnUiThread {
                    Toast.makeText(this@EditarUsuarioAdminActivity,
                        "Cliente actualizado ✓", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@EditarUsuarioAdminActivity,
                        "Error al actualizar: ${e.message}",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}