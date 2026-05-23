package com.example.e_commerceapp.admin

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_commerceapp.SupabaseClient
import com.example.e_commerceapp.databinding.ActivityAddUserBinding
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import java.util.Calendar

class AddUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDropdowns()
        setupFecha()
        setupBotones()
    }

    private fun setupDropdowns() {
        val roles = listOf("Cliente", "Vendedor", "Administrador")
        binding.spinnerRol.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        )
        binding.spinnerRol.setOnClickListener { binding.spinnerRol.showDropDown() }

        val estados = listOf("Activo", "Inactivo", "Pendiente")
        binding.spinnerEstado.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, estados)
        )
        binding.spinnerEstado.setOnClickListener { binding.spinnerEstado.showDropDown() }
    }

    private fun setupFecha() {
        binding.etFechaNacimiento.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                binding.etFechaNacimiento.setText("$day/${month + 1}/$year")
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun setupBotones() {
        binding.btnBack.setOnClickListener { finish() }
        binding.btnCancelar.setOnClickListener { finish() }

        binding.btnGuardar.setOnClickListener {
            val nombres   = binding.etNombres.text.toString().trim()
            val apellidos = binding.etApellidos.text.toString().trim()
            val correo    = binding.etCorreo.text.toString().trim()
            val password  = binding.etPassword.text.toString().trim()
            val telefono  = binding.etTelefono.text.toString().trim()
            val estado    = binding.spinnerEstado.text.toString().trim()

            if (nombres.isEmpty() || apellidos.isEmpty() ||
                correo.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa los campos obligatorios",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 8) {
                Toast.makeText(this, "La contraseña debe tener mínimo 8 caracteres",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            crearUsuario(nombres, apellidos, correo, password, telefono, estado)
        }
    }

    private fun crearUsuario(
        nombres: String, apellidos: String, correo: String,
        password: String, telefono: String, estado: String
    ) {
        lifecycleScope.launch {
            try {
                // Paso 1: crear usuario en Supabase Auth
                SupabaseClient.client.auth.signUpWith(Email) {
                    email    = correo
                    this.password = password
                }

                // Paso 2: obtener el id del usuario recién creado
                val userId = SupabaseClient.client.auth.currentUserOrNull()?.id ?: ""

                // Paso 3: insertar en tabla Usuarios
                SupabaseClient.client.postgrest["Usuarios"]
                    .insert(mapOf(
                        "id"       to userId,
                        "nombre"   to nombres,
                        "apellido" to apellidos,
                        "correo"   to correo,
                        "telefono" to telefono,
                        "estado"   to estado.ifEmpty { "Activo" }
                    ))

                runOnUiThread {
                    Toast.makeText(this@AddUserActivity,
                        "Usuario creado ✓", Toast.LENGTH_SHORT).show()
                    finish()
                }

            } catch (e: Exception) {
                android.util.Log.e("ADD_USER", "Error: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@AddUserActivity,
                        "Error al crear usuario: ${e.message}",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}