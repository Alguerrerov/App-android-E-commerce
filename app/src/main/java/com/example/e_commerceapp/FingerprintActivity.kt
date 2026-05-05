package com.example.e_commerceapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.*
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

class FingerprintActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fingerprint)

        // "Usar contraseña" → regresa al login
        findViewById<TextView>(R.id.tvUsePassword).setOnClickListener {
            finish()
        }

        // Lanzar biometría automáticamente al entrar
        checkAndLaunchBiometric()
    }

    private fun checkAndLaunchBiometric() {
        val biometricManager = BiometricManager.from(this)

        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> showBiometricPrompt()

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(this, "No hay huellas registradas en el dispositivo", Toast.LENGTH_LONG).show()
                finish()
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(this, "Este dispositivo no soporta huella digital", Toast.LENGTH_LONG).show()
                finish()
            }

            else -> finish()
        }
    }

    private fun showBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)

        val prompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    // ✅ Ir a la pantalla principal
                    startActivity(Intent(this@FingerprintActivity, MainActivity::class.java))
                    finish()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    // Usuario canceló o error grave → regresa al login
                    if (errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON &&
                        errorCode != BiometricPrompt.ERROR_USER_CANCELED) {
                        Toast.makeText(applicationContext, "Error: $errString", Toast.LENGTH_SHORT).show()
                    }
                    finish()
                }

                override fun onAuthenticationFailed() {
                    // Huella no reconocida — el diálogo del sistema ya lo indica, no hace falta Toast
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("AutoParts")
            .setSubtitle("Verifica tu identidad")
            .setNegativeButtonText("Cancelar")
            .setAllowedAuthenticators(BIOMETRIC_STRONG or BIOMETRIC_WEAK)
            .build()

        prompt.authenticate(promptInfo)
    }
}