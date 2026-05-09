package com.example.e_commerceapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.databinding.ActivityMiDireccionBinding

class MiDireccionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMiDireccionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMiDireccionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.tvAgregarDireccion.setOnClickListener {
            Toast.makeText(this, "Agregar nueva dirección", Toast.LENGTH_SHORT).show()
        }
        binding.btnEditarCasa.setOnClickListener {
            Toast.makeText(this, "Editar Casa", Toast.LENGTH_SHORT).show()
        }
        binding.btnEliminarCasa.setOnClickListener {
            Toast.makeText(this, "Eliminar Casa", Toast.LENGTH_SHORT).show()
        }
        binding.btnEditarTrabajo.setOnClickListener {
            Toast.makeText(this, "Editar Trabajo", Toast.LENGTH_SHORT).show()
        }
        binding.btnEliminarTrabajo.setOnClickListener {
            Toast.makeText(this, "Eliminar Trabajo", Toast.LENGTH_SHORT).show()
        }
        binding.btnGuardar.setOnClickListener {
            Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}