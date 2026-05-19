package com.example.e_commerceapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.auth.LoginActivity
import com.example.e_commerceapp.databinding.ActivityEliminarCuentaBinding

class EliminarCuentaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEliminarCuentaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEliminarCuentaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        // Habilitar botón eliminar solo si acepta el checkbox
        binding.checkConfirmar.setOnCheckedChangeListener { _, isChecked ->
            binding.btnEliminar.isEnabled = isChecked
            binding.btnEliminar.alpha = if (isChecked) 1f else 0.5f
        }

        binding.btnEliminar.setOnClickListener {
            Toast.makeText(this, "Cuenta eliminada", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.btnCancelar.setOnClickListener { finish() }
    }
}