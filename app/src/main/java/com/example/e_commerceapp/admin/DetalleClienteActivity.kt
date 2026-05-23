package com.example.e_commerceapp.admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.databinding.ActivityDetalleClienteBinding
import com.example.e_commerceapp.admin.OpcionesClienteActivity

class DetalleClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleClienteBinding
    private var clienteId     = ""
    private var estadoActual  = "Activo"

    private val editarLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clienteId    = intent.getStringExtra("id")     ?: ""
        estadoActual = intent.getStringExtra("estado") ?: "Activo"

        val nombre        = intent.getStringExtra("nombre")        ?: ""
        val apellido      = intent.getStringExtra("apellido")      ?: ""
        val email         = intent.getStringExtra("email")         ?: ""
        val telefono      = intent.getStringExtra("telefono")      ?: ""
        val genero        = intent.getStringExtra("genero")        ?: ""
        val dni           = intent.getStringExtra("dni")           ?: ""
        val fechaRegistro = intent.getStringExtra("fechaRegistro") ?: ""
        val direccion     = intent.getStringExtra("direccion")     ?: ""

        binding.tvNombre.text             = "$nombre $apellido"
        binding.tvNombres.text            = nombre
        binding.tvApellidos.text          = apellido
        binding.tvEmail.text              = email
        binding.tvTelefono.text           = telefono
        binding.tvDni.text                = dni
        binding.tvFecha.text              = fechaRegistro
        binding.tvEstado.text             = estadoActual
        binding.tvDireccion.text          = direccion
        binding.tvId.text                 = "ID: ${clienteId.take(8)}..."

        setupBotones(nombre, apellido, email, telefono, genero, dni, fechaRegistro, direccion)
    }

    private fun setupBotones(
        nombre: String, apellido: String, email: String,
        telefono: String, genero: String, dni: String,
        fechaRegistro: String, direccion: String
    ) {
        binding.btnBack.setOnClickListener { finish() }

        binding.btnEditar.setOnClickListener {
            val intent = Intent(this, OpcionesClienteActivity::class.java)
            intent.putExtra("id",            clienteId)
            intent.putExtra("nombre",        nombre)
            intent.putExtra("apellido",      apellido)
            intent.putExtra("email",         email)
            intent.putExtra("telefono",      telefono)
            intent.putExtra("genero",        genero)
            intent.putExtra("dni",           dni)
            intent.putExtra("fechaRegistro", fechaRegistro)
            intent.putExtra("estado",        estadoActual)
            intent.putExtra("direccion",     direccion)
            startActivity(intent)
        }
    }
}