package com.example.e_commerceapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.databinding.ActivityLoginBinding
class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupTabs()
        setupButtons()
    }

    private fun setupTabs() {
        binding.tabCorreo.setOnClickListener {
            binding.tabCorreo.isSelected = true
            binding.tabTelefono.isSelected = false
            binding.etInput.inputType =
                android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            binding.tilInput.hint = "Correo electrónico"
        }
        binding.tabTelefono.setOnClickListener {
            binding.tabTelefono.isSelected = true
            binding.tabCorreo.isSelected = false
            binding.etInput.inputType =
                android.text.InputType.TYPE_CLASS_PHONE
            binding.tilInput.hint = "Número de teléfono"
        }
    }

    private fun setupButtons() {
        binding.btnLogin.setOnClickListener {
            val input = binding.etInput.text.toString().trim()
            val pass  = binding.etPassword.text.toString().trim()
            if (input.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // TODO: conectar con Firebase o tu backend
            Toast.makeText(this, "Iniciando sesión...", Toast.LENGTH_SHORT).show()
        }
        binding.btnGoogle.setOnClickListener {
            Toast.makeText(this, "Google Sign-In próximamente", Toast.LENGTH_SHORT).show()
        }
        binding.btnFacebook.setOnClickListener {
            Toast.makeText(this, "Facebook Login próximamente", Toast.LENGTH_SHORT).show()
        }
        binding.llFingerprint.setOnClickListener {
            Toast.makeText(this, "Huella digital próximamente", Toast.LENGTH_SHORT).show()
        }
        binding.tvForgot.setOnClickListener {
            Toast.makeText(this, "Recuperar contraseña", Toast.LENGTH_SHORT).show()
        }
    }
}