package com.example.e_commerceapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.databinding.ActivityAddUserBinding
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
        // Roles
        val roles = listOf("Cliente", "Vendedor", "Administrador")
        binding.spinnerRol.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        )
        // Estados
        val estados = listOf("Activo", "Inactivo", "Pendiente")
        binding.spinnerEstado.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, estados)
        )
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
            val nombres = binding.etNombres.text.toString().trim()
            val apellidos = binding.etApellidos.text.toString().trim()
            val correo = binding.etCorreo.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

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
            // TODO: guardar en base de datos
            Toast.makeText(this, "Usuario guardado correctamente",
                Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}