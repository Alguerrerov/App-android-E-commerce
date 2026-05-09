package com.example.e_commerceapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.databinding.ActivityNotificacionesBinding

class NotificacionesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificacionesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificacionesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        listOf(binding.switchOfertas, binding.switchPedidos,
            binding.switchNuevosProductos, binding.switchCarrito,
            binding.switchSoporte, binding.switchNoticias
        ).forEach { switch ->
            switch.setOnCheckedChangeListener { _, isChecked ->
                Toast.makeText(this,
                    if (isChecked) "Activado" else "Desactivado",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}