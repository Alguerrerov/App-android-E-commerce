package com.example.e_commerceapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.databinding.ActivityAccesibilidadBinding

class AccesibilidadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccesibilidadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccesibilidadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.itemTamanoTexto.setOnClickListener {
            Toast.makeText(this, "Tamaño de texto", Toast.LENGTH_SHORT).show()
        }

        binding.itemContactarSoporte.setOnClickListener {
            Toast.makeText(this, "Contactar soporte", Toast.LENGTH_SHORT).show()
        }

        binding.switchAltoContraste.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this,
                if (isChecked) "Alto contraste activado" else "Alto contraste desactivado",
                Toast.LENGTH_SHORT).show()
        }

        binding.switchReducirAnimaciones.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this,
                if (isChecked) "Animaciones reducidas" else "Animaciones normales",
                Toast.LENGTH_SHORT).show()
        }
    }
}