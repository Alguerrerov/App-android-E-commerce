package com.example.e_commerceapp.admin

import android.R
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.databinding.ActivityDetallePedidoAdminBinding

class DetallePedidoAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetallePedidoAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallePedidoAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val numero     = intent.getStringExtra("numero")     ?: ""
        val cliente    = intent.getStringExtra("cliente")    ?: ""
        val email      = intent.getStringExtra("email")      ?: ""
        val telefono   = intent.getStringExtra("telefono")   ?: ""
        val fecha      = intent.getStringExtra("fecha")      ?: ""
        val total      = intent.getStringExtra("total")      ?: ""
        val subtotal   = intent.getStringExtra("subtotal")   ?: ""
        val envio      = intent.getStringExtra("envio")      ?: ""
        val estado     = intent.getStringExtra("estado")     ?: ""
        val direccion  = intent.getStringExtra("direccion")  ?: ""
        val referencia = intent.getStringExtra("referencia") ?: ""
        val metodoPago = intent.getStringExtra("metodoPago") ?: ""

        binding.tvNumeroPedido.text    = numero
        binding.tvFecha.text           = fecha
        binding.tvEstado.text          = estado
        binding.tvNombreCliente.text   = cliente
        binding.tvEmailCliente.text    = "✉ $email"
        binding.tvTelefonoCliente.text = "📞 $telefono"
        binding.tvSubtotal.text        = subtotal
        binding.tvEnvio.text           = envio
        binding.tvTotal.text           = total
        binding.tvDireccion.text       = "📍 $direccion"
        binding.tvReferencia.text      = "☑ Referencia: $referencia"
        binding.tvMetodoPago.text      = metodoPago

        val bgEstado = when (estado) {
            "Pendiente"  -> "#E67E22"
            "En proceso" -> "#1E90FF"
            "Completado" -> "#4CAF50"
            "Cancelado"  -> "#E74C3C"
            else         -> "#8899AA"
        }
        binding.tvEstado.setBackgroundColor(Color.parseColor(bgEstado))

        val estados = listOf("Pendiente", "En proceso", "Completado", "Cancelado")
        binding.spinnerEstado.adapter = ArrayAdapter(this,
            R.layout.simple_spinner_item, estados).also {
            it.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerEstado.setSelection(estados.indexOf(estado).coerceAtLeast(0))

        binding.btnActualizar.setOnClickListener {
            Toast.makeText(this, "Estado actualizado", Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.btnCancelar.setOnClickListener { finish() }
        binding.btnBack.setOnClickListener { finish() }
    }
}