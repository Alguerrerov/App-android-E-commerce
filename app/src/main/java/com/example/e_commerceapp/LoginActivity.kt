package com.example.e_commerceapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.databinding.ActivityLoginBinding
import android.content.Intent
import kotlin.jvm.java
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.*
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Seleccionar tab Correo por defecto al abrir
        binding.tabCorreo.isSelected = true
        binding.tabTelefono.isSelected = false

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
            // Validar credenciales
            val input = binding.etInput.text.toString().trim()
            val pass  = binding.etPassword.text.toString().trim()

            if (input.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //////usarios permitidos////
            data class User(val name: String, val pass: String, val targetActivity: Class<*>)

            val usuariosPermitidos = listOf(
                User("admin", "admin", AdminMainActivity::class.java),
                User("user", "user", ClientsMainActivity::class.java),
                User("ventas", "ventas", SellersMainActivity::class.java)
            )

            val usuarioEncontrado = usuariosPermitidos.find { it.name == input && it.pass == pass }

            if (usuarioEncontrado != null) {
                val intent = Intent(this,usuarioEncontrado.targetActivity )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Credenciales incorrectas",
                    Toast.LENGTH_SHORT).show()
            }
        }

        //olvide mi contraseña
        binding.tvForgot.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        //Biometria
        binding.llFingerprint.setOnClickListener {
            startActivity(Intent(this, FingerprintActivity::class.java))
        }


    }
}