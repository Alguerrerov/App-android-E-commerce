package com.example.e_commerceapp.client

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_commerceapp.SupabaseClient
import com.example.e_commerceapp.databinding.ActivityPagoWebviewBinding
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.UUID

class PagoWebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPagoWebviewBinding

    // Llaves de prueba públicas de Wompi
    private val WOMPI_PUBLIC_KEY_TEST = "pub_test_yourkey"  // ← reemplaza con tu llave test de Wompi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagoWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val total     = intent.getDoubleExtra("total", 0.0)
        val referencia = "ORDER-${UUID.randomUUID().toString().take(8).uppercase()}"

        binding.btnBack.setOnClickListener { finish() }

        configurarWebView(total, referencia)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configurarWebView(total: Double, referencia: String) {
        // Wompi recibe el monto en centavos
        val totalCentavos = (total * 100).toLong()

        val urlWompi = "https://checkout.wompi.co/p/" +
                "?public-key=$WOMPI_PUBLIC_KEY_TEST" +
                "&currency=COP" +
                "&amount-in-cents=$totalCentavos" +
                "&reference=$referencia" +
                "&redirect-url=https://tu-app.com/confirmacion"

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled  = true

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?, request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()

                // Detectar cuando Wompi redirige a la URL de confirmación
                if (url.contains("tu-app.com/confirmacion")) {
                    val uri    = android.net.Uri.parse(url)
                    val status = uri.getQueryParameter("status") ?: ""
                    val ref    = uri.getQueryParameter("reference") ?: referencia

                    when (status) {
                        "APPROVED" -> confirmarPago(ref, total, "APROBADO")
                        "DECLINED" -> mostrarResultado(false, "Pago rechazado")
                        "VOIDED"   -> mostrarResultado(false, "Pago cancelado")
                        else       -> mostrarResultado(false, "Estado desconocido: $status")
                    }
                    return true
                }
                return false
            }
        }

        binding.webView.loadUrl(urlWompi)
    }

    private fun confirmarPago(referencia: String, total: Double, estado: String) {
        lifecycleScope.launch {
            try {
                val userId = SupabaseClient.client.auth.currentUserOrNull()?.id ?: ""

                // Guardar el pago en Supabase
                SupabaseClient.client.postgrest["Pagos"]
                    .insert(mapOf(
                        "usuario_id"  to userId,
                        "referencia"  to referencia,
                        "total"       to total,
                        "estado"      to estado,
                        "proveedor"   to "Wompi"
                    ))

                runOnUiThread {
                    mostrarResultado(true, "¡Pago aprobado! Referencia: $referencia")
                }
            } catch (e: Exception) {
                runOnUiThread {
                    // Aunque falle el guardado, el pago fue aprobado
                    mostrarResultado(true, "¡Pago aprobado! Referencia: $referencia")
                }
            }
        }
    }

    private fun mostrarResultado(exitoso: Boolean, mensaje: String) {
        android.app.AlertDialog.Builder(this)
            .setTitle(if (exitoso) "✅ Pago exitoso" else "❌ Pago fallido")
            .setMessage(mensaje)
            .setPositiveButton("Aceptar") { _, _ ->
                if (exitoso) {
                    setResult(RESULT_OK)
                    finish()
                }
            }
            .setCancelable(false)
            .show()
    }
}