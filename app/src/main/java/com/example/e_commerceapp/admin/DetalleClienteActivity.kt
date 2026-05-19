package com.example.e_commerceapp.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.client.OpcionesClienteActivity
import com.example.e_commerceapp.databinding.ActivityDetalleClienteBinding

class DetalleClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleClienteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nombre = intent.getStringExtra("nombre") ?: "Cliente"
        val email  = intent.getStringExtra("email")  ?: ""
        val rol    = intent.getStringExtra("rol")    ?: "Cliente"
        val estado = intent.getStringExtra("estado") ?: "Activo"

        binding.tvNombre.text  = nombre
        binding.tvEmail.text   = email
        binding.tvRol.text     = rol
        binding.tvEstado.text  = estado
        binding.tvNombres.text = nombre.split(" ").firstOrNull() ?: nombre
        binding.tvApellidos.text = nombre.split(" ").drop(1).joinToString(" ")

        binding.btnBack.setOnClickListener { finish() }

        // Botón editar → también abre opciones
        binding.btnEditar.setOnClickListener {
            val intent = Intent(this, OpcionesClienteActivity::class.java)
            intent.putExtra("nombre", nombre)
            intent.putExtra("email",  email)
            intent.putExtra("rol",    rol)
            intent.putExtra("estado", estado)
            startActivity(intent)
        }
    }
}