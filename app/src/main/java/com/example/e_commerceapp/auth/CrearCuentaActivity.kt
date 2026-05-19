package com.example.e_commerceapp.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_commerceapp.databinding.ActivityCrearCuentaBinding
import com.example.e_commerceapp.R
import com.example.e_commerceapp.SupabaseClient
import com.example.e_commerceapp.model.UsuarioData
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import io.github.jan.supabase.auth.providers.builtin.Email



class CrearCuentaActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var checkTerminos: CheckBox
    private lateinit var btnCrearCuenta: Button
    private lateinit var btnBack: ImageButton
    private lateinit var btnGoogle: Button
    private lateinit var btnFacebook: Button
    private lateinit var tvTerminos: TextView
    private lateinit var tvPrivacidad: TextView
    private lateinit var tvIniciarSesion: TextView


    private lateinit var binding: ActivityCrearCuentaBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)

        // Inicializar vistas con findViewById
        etNombre           = findViewById(R.id.etNombre)
        etApellido         = findViewById(R.id.etApellido)
        etCorreo           = findViewById(R.id.etCorreo)
        etTelefono         = findViewById(R.id.etTelefono)
        etPassword         = findViewById(R.id.etPassword)
        etConfirmPassword  = findViewById(R.id.etConfirmPassword)
        checkTerminos      = findViewById(R.id.checkTerminos)
        btnCrearCuenta     = findViewById(R.id.btnCrearCuenta)
        btnBack           = findViewById(R.id.btnBack)
        btnGoogle         = findViewById(R.id.btnGoogle)
        btnFacebook       = findViewById(R.id.btnFacebook)
        tvTerminos        = findViewById(R.id.tvTerminos)
        tvPrivacidad      = findViewById(R.id.tvPrivacidad)
        tvIniciarSesion   = findViewById(R.id.tvIniciarSesion)


        setupBotones()
    }

    private fun setupBotones() {
        btnBack.setOnClickListener { finish() }

        tvTerminos.setOnClickListener {
            Toast.makeText(this, "Términos y Condiciones", Toast.LENGTH_SHORT).show()
        }

        tvPrivacidad.setOnClickListener {
            Toast.makeText(this, "Política de Privacidad", Toast.LENGTH_SHORT).show()
        }

        btnGoogle.setOnClickListener {
            Toast.makeText(this, "Registro con Google próximamente",
                Toast.LENGTH_SHORT).show()
        }

        btnFacebook.setOnClickListener {
            Toast.makeText(this, "Registro con Facebook próximamente",
                Toast.LENGTH_SHORT).show()
        }

        tvIniciarSesion.setOnClickListener {
            finish() // regresa al login
        }

        btnCrearCuenta.setOnClickListener {
           if (validarCampos()) {
            crearCuenta()
            }
        }
    }

    private fun validarCampos(): Boolean {
        val nombre     = etNombre.text.toString().trim()
        val correo     = etCorreo.text.toString().trim()
        val telefono   = etTelefono.text.toString().trim()
        val contrasena = etPassword.text.toString().trim()
        val confirm    = etConfirmPassword.text.toString().trim()

        if (nombre.isEmpty()) {
            etNombre.error = "Ingresa tu nombre"
            return false
        }
        if (correo.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS
                .matcher(correo).matches()) {
            etCorreo.error = "Ingresa un correo válido"
            return false
        }
        if (telefono.isEmpty() || telefono.length < 9) {
            etTelefono.error = "Ingresa un teléfono válido"
            return false
        }
        if (contrasena.isEmpty() || contrasena.length < 8) {
            etPassword.error = "Mínimo 8 caracteres"
            return false
        }
        if (contrasena != confirm) {
            etConfirmPassword.error = "Las contraseñas no coinciden"
            return false
        }
        if (!checkTerminos.isChecked) {
            Toast.makeText(this,
                "Debes aceptar los Términos y Condiciones",
                Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun crearCuenta() {

        val nombre   = etNombre.text.toString().trim()
        val apellido = etApellido.text.toString().trim()
        val correo   = etCorreo.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val contrasena = etPassword.text.toString().trim()
        val confirm  = etConfirmPassword.text.toString().trim()

        // Registrar en supabase
        lifecycleScope.launch {
            try {
                //Registro correo y contraseña en supabase
                SupabaseClient.client.auth.signUpWith(Email){
                    email = correo
                    password = contrasena
                }

                // obtener uuid generado y guardar datos
                val userId= SupabaseClient.client.auth.currentUserOrNull()?.id ?: ""
                SupabaseClient.client.postgrest["Usuarios"].insert(
                    UsuarioData(
                    id = userId,
                    nombre = nombre,
                    apellido = apellido,
                    correo = correo,
                    telefono = telefono

                ))

                //redirigir a login
                runOnUiThread {
                    Toast.makeText(this@CrearCuentaActivity,"Registro exitoso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@CrearCuentaActivity, LoginActivity::class.java))
                    finish()
                }
            } catch (e: Exception){
                android.util.Log.e("SUPABASE", "Error: ${e.message}")
                android.util.Log.e("SUPABASE", "Causa: ${e.cause}")
                android.util.Log.e("SUPABASE", "Stack: ${e.stackTraceToString()}")
                runOnUiThread {
                    Toast.makeText(this@CrearCuentaActivity,"Error al crear la cuenta", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}