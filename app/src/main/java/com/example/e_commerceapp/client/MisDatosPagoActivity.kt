package com.example.e_commerceapp.client

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.databinding.ActivityMisDatosPagoBinding

class MisDatosPagoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMisDatosPagoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMisDatosPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.tvAgregarMetodo.setOnClickListener {
            Toast.makeText(this, "Agregar método de pago", Toast.LENGTH_SHORT).show()
        }
        binding.btnEditarVisa.setOnClickListener {
            Toast.makeText(this, "Editar Visa", Toast.LENGTH_SHORT).show()
        }
        binding.btnEliminarVisa.setOnClickListener {
            Toast.makeText(this, "Eliminar Visa", Toast.LENGTH_SHORT).show()
        }
        binding.btnEditarMC.setOnClickListener {
            Toast.makeText(this, "Editar Mastercard", Toast.LENGTH_SHORT).show()
        }
        binding.btnEliminarMC.setOnClickListener {
            Toast.makeText(this, "Eliminar Mastercard", Toast.LENGTH_SHORT).show()
        }
        binding.btnEditarNequi.setOnClickListener {
            Toast.makeText(this, "Editar Nequi", Toast.LENGTH_SHORT).show()
        }
        binding.btnEliminarNequi.setOnClickListener {
            Toast.makeText(this, "Eliminar Nequi", Toast.LENGTH_SHORT).show()
        }
        binding.btnGuardar.setOnClickListener {
            Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}