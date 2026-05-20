package com.example.e_commerceapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_commerceapp.SupabaseClient
import com.example.e_commerceapp.databinding.ActivityInformacionPersonalBinding
import com.example.e_commerceapp.model.UsuarioData
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch

class InformacionPersonalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInformacionPersonalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformacionPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarDatosUsuario()
        setupBotones()
    }

    private fun cargarDatosUsuario() {
        // Leer datos guardados en SharedPreferences
        val prefs    = getSharedPreferences("autoparts_prefs", MODE_PRIVATE)
        val nombre   = prefs.getString("user_nombre", "Sin nombre") ?: "Sin nombre"
        val correo   = prefs.getString("user_correo", "Sin correo") ?: "Sin correo"
        val telefono = prefs.getString("user_telefono", "Sin teléfono") ?: "Sin teléfono"
        val fecha    = prefs.getString("user_fecha", "No especificada") ?: "No especificada"
        val genero   = prefs.getString("user_genero", "No especificado") ?: "No especificado"

        // Mostrar en la UI
        binding.tvNombre.text   = nombre
        binding.tvCorreo.text   = correo
        binding.tvTelefono.text = telefono
        binding.tvFecha.text    = fecha
        binding.tvGenero.text   = genero
    }

    private fun setupBotones() {
        binding.btnBack.setOnClickListener { finish() }

        binding.btnGuardar.setOnClickListener {
            guardarCambiosEnSupabase()
        }
    }

    private fun guardarCambiosEnSupabase() {
        val prefs    = getSharedPreferences("autoparts_prefs", MODE_PRIVATE)
        val userId   = prefs.getString("user_id", "") ?: ""
        val nombre   = binding.tvNombre.text.toString().trim()
        val correo   = binding.tvCorreo.text.toString().trim()
        val telefono = binding.tvTelefono.text.toString().trim()

        if (userId.isEmpty()) {
            Toast.makeText(this, "Error: usuario no identificado",
                Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                // Actualizar en Supabase
                SupabaseClient.client.postgrest["Usuarios"]
                    .update(
                        UsuarioData(
                            id = userId,
                            nombre = nombre,
                            correo = correo,
                            telefono = telefono
                        )
                    ) {
                        filter {
                            eq("id", userId)
                        }
                    }

                // Actualizar SharedPreferences con los nuevos datos
                prefs.edit()
                    .putString("user_nombre", nombre)
                    .putString("user_correo", correo)
                    .putString("user_telefono", telefono)
                    .apply()

                runOnUiThread {
                    Toast.makeText(this@InformacionPersonalActivity,
                        "Cambios guardados ✓", Toast.LENGTH_SHORT).show()
                    finish()
                }

            } catch (e: Exception) {
                android.util.Log.e("PERFIL", "Error: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@InformacionPersonalActivity,
                        "Error al guardar: ${e.message}",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}