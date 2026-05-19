package com.example.e_commerceapp.client

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.databinding.ActivityOpcionesClienteBinding

class OpcionesClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOpcionesClienteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpcionesClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nombre = intent.getStringExtra("nombre") ?: "Cliente"
        val email  = intent.getStringExtra("email")  ?: ""
        val rol    = intent.getStringExtra("rol")    ?: "Cliente"
        val estado = intent.getStringExtra("estado") ?: "Activo"

        binding.tvNombre.text       = nombre
        binding.tvEmail.text        = email
        binding.tvRol.text          = rol
        binding.tvEstadoActual.text = estado

        binding.btnBack.setOnClickListener { finish() }

        binding.cardEditar.setOnClickListener {
            Toast.makeText(this, "Editar información de $nombre", Toast.LENGTH_SHORT).show()
        }

        binding.cardHistorial.setOnClickListener {
            Toast.makeText(this, "Historial de pedidos de $nombre", Toast.LENGTH_SHORT).show()
        }

        binding.cardDirecciones.setOnClickListener {
            Toast.makeText(this, "Direcciones de $nombre", Toast.LENGTH_SHORT).show()
        }

        binding.cardBloquear.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Bloquear cliente")
                .setMessage("¿Seguro que deseas bloquear a $nombre? No podrá acceder a la app.")
                .setPositiveButton("Bloquear") { _, _ ->
                    binding.tvEstadoActual.text = "Bloqueado"
                    binding.tvEstadoActual.setTextColor(Color.parseColor("#E24B4A"))
                    Toast.makeText(this, "$nombre bloqueado", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        binding.cardEliminar.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar cliente")
                .setMessage("¿Seguro que deseas eliminar a $nombre? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar") { _, _ ->
                    Toast.makeText(this, "$nombre eliminado", Toast.LENGTH_SHORT).show()
                    finishAffinity()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}