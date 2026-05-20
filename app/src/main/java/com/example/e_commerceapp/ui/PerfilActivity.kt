package com.example.e_commerceapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.auth.LoginActivity
import com.example.e_commerceapp.client.MiDireccionActivity
import com.example.e_commerceapp.client.MisDatosPagoActivity
import com.example.e_commerceapp.client.MisPedidosActivity
import com.example.e_commerceapp.databinding.ActivityPerfilBinding

class PerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar datos del usuario desde SharedPreferences
        cargarDatosUsuario()

        binding.tvEditarPerfil.setOnClickListener {
            startActivity(Intent(this, InformacionPersonalActivity::class.java))
        }
        binding.itemMisPedidos.setOnClickListener {
            startActivity(Intent(this, MisPedidosActivity::class.java))
        }
        binding.itemMiDireccion.setOnClickListener {
            startActivity(Intent(this, MiDireccionActivity::class.java))
        }
        binding.itemDatosPago.setOnClickListener {
            startActivity(Intent(this, MisDatosPagoActivity::class.java))
        }
        binding.itemConfiguracion.setOnClickListener {
            startActivity(Intent(this, ConfiguracionActivity::class.java))
        }
        binding.itemCerrarSesion.setOnClickListener {
            // Limpiar datos al cerrar sesión
            cerrarSesion()
        }
    }

    private fun cargarDatosUsuario() {
        val prefs   = getSharedPreferences("autoparts_prefs", MODE_PRIVATE)
        val nombre  = prefs.getString("user_nombre", "Usuario") ?: "Usuario"
        val correo  = prefs.getString("user_correo", "") ?: ""

        binding.tvNombre.text = nombre
        binding.tvEmail.text  = correo
    }

    private fun cerrarSesion() {
        // Limpiar SharedPreferences al cerrar sesión
        val prefs = getSharedPreferences("autoparts_prefs", MODE_PRIVATE)
        prefs.edit().clear().apply()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}