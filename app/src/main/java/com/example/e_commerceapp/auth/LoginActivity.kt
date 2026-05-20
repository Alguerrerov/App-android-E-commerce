package com.example.e_commerceapp.auth

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_commerceapp.SupabaseClient
import com.example.e_commerceapp.admin.AdminMainActivity
import com.example.e_commerceapp.client.ClientsMainActivity
import com.example.e_commerceapp.seller.SellersMainActivity
import com.example.e_commerceapp.databinding.ActivityLoginBinding
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest

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
                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            binding.tilInput.hint = "Correo electrónico"
        }
        binding.tabTelefono.setOnClickListener {
            binding.tabTelefono.isSelected = true
            binding.tabCorreo.isSelected = false
            binding.etInput.inputType =
                InputType.TYPE_CLASS_PHONE
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

            // Credenciales para admin y vendedor
            when {
                input == "admin" && pass == "admin" -> {
                    navegarA(AdminMainActivity::class.java, "Admin", input)
                }
                input == "ventas" && pass == "ventas" -> {
                    navegarA(SellersMainActivity::class.java, "Vendedor", input)
                }
                else -> {
                    // Login con Supabase para clientes
                    loginConSupabase(input, pass)
                }
            }
        }

        //crear cuenta
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, CrearCuentaActivity::class.java))
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

    private fun loginConSupabase(correo: String, contrasena: String) {
        lifecycleScope.launch {
            try {

                // 1. Autenticar con Supabase
                SupabaseClient.client.auth.signInWith(Email) {
                    email    = correo
                    password = contrasena
                }
                android.util.Log.d("LOGIN", "Auth OK")

                // 2. Obtener userId
                val userId = SupabaseClient.client.auth
                    .currentUserOrNull()?.id ?: ""
                android.util.Log.d("LOGIN", "UserId: $userId")

                // 3. Consultar datos del usuario en la tabla Usuarios
                val usuario = SupabaseClient.client
                    .postgrest["Usuarios"]
                    .select {
                        filter {
                            eq("id", userId)
                        }
                    }
                    .decodeSingle<com.example.e_commerceapp.model.UsuarioData>()
                android.util.Log.d("LOGIN", "Datos obtenidos: ${usuario.nombre}")

                var esAdmin = false
                try {
                    // buscar el ID en la tabla admin
                    val adminCheck = SupabaseClient.client
                        .postgrest["Administradores"]
                        .select {
                            filter {
                                eq("id", userId)
                            }
                        }

                    // confirmar que es administrador
                    esAdmin = adminCheck.data != "[]"
                } catch (e: Exception) {
                    android.util.Log.e("LOGIN", "Error al verificar admin (puede que no exista en la tabla): ${e.message}")
                    esAdmin = false
                }
                android.util.Log.d("LOGIN", "¿Es administrador?: $esAdmin")

                // 4. Guardar datos en SharedPreferences
                val prefs = getSharedPreferences("autoparts_prefs", MODE_PRIVATE)
                prefs.edit()
                    .putString("user_id", userId)
                    .putString("user_nombre", usuario.nombre)
                    .putString("user_correo", usuario.correo)
                    .putString("user_telefono", usuario.telefono)
                    .apply()

                // 5. Navegar segun el tipo de usuario
                runOnUiThread {
                    Toast.makeText(this@LoginActivity,
                        "Bienvenido ${usuario.nombre} ✓",
                        Toast.LENGTH_SHORT).show()

                    if (esAdmin){
                        navegarA(AdminMainActivity::class.java,
                            usuario.nombre, correo)
                    }else{
                    navegarA(ClientsMainActivity::class.java,
                        usuario.nombre, correo)
                    }
                }

            } catch (e: Exception) {
                android.util.Log.e("LOGIN", "Error: ${e.message}")
                runOnUiThread {
                    val mensaje = when {
                        e.message?.contains("invalid_credentials") == true ->
                            "Correo o contraseña incorrectos"
                        e.message?.contains("email_not_confirmed") == true ->
                            "Debes confirmar tu correo primero"
                        else -> "Error al iniciar sesión: ${e.message}"
                    }
                    Toast.makeText(this@LoginActivity,
                        mensaje, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun navegarA(destino: Class<*>, nombre: String, correo: String) {
        val intent = Intent(this, destino)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}