package com.example.e_commerceapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.databinding.ActivityConfiguracionBinding

class ConfiguracionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfiguracionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguracionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.itemNotificaciones.setOnClickListener {
            startActivity(Intent(this, NotificacionesActivity::class.java))
        }
        binding.itemIdioma.setOnClickListener {
            startActivity(Intent(this, IdiomaActivity::class.java))
        }
        binding.itemAccesibilidad.setOnClickListener {
            startActivity(Intent(this, AccesibilidadActivity::class.java))
        }
        binding.itemActivarVendedor.setOnClickListener {
            startActivity(Intent(this, ActivarVendedorActivity::class.java))
        }
        binding.itemEliminarCuenta.setOnClickListener {
            startActivity(Intent(this, EliminarCuentaActivity::class.java))
        }
    }
}