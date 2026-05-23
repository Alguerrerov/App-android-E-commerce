package com.example.e_commerceapp.client

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.R
import com.example.e_commerceapp.databinding.ActivityResumenCompraBinding

class ResumenCompraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResumenCompraBinding

    private val productos = listOf(
        ItemCarrito("Filtro de aceite Mann W712",       "FIL-MANN-W712",   45.0,  1),
        ItemCarrito("Pastillas de freno Brembo P85020", "BRM-P85020",      120.0, 2),
        ItemCarrito("Aceite Mobil 1 5W-30 (1L)",        "ACE-MOBIL1-5W30", 65.0,  1),
        ItemCarrito("Filtro de aire Mann C 27 009",     "FIL-MANN-C27009", 55.0,  1)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumenCompraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cargarProductosResumen()
        setupBotones()
    }

    private fun cargarProductosResumen() {
        val subtotal = productos.sumOf { it.precioUnitario * it.cantidad }
        val total    = subtotal + 15.0

        binding.tvSubtotal.text = "S/ ${String.format("%.2f", subtotal)}"
        binding.tvTotal.text    = "S/ ${String.format("%.2f", total)}"

        // Agregar filas de productos dinámicamente
        productos.forEach { item ->
            val fila = LayoutInflater.from(this)
                .inflate(R.layout.item_resumen_producto, binding.llProductosResumen, false)

            fila.findViewById<TextView>(R.id.tvNombre).text   = item.nombre
            fila.findViewById<TextView>(R.id.tvSku).text      = "SKU: ${item.sku}"
            fila.findViewById<TextView>(R.id.tvCantidad).text = "x${item.cantidad}"
            fila.findViewById<TextView>(R.id.tvPrecio).text   =
                "S/ ${String.format("%.2f", item.precioUnitario * item.cantidad)}"

            binding.llProductosResumen.addView(fila)
        }
    }

    private val pagoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            Toast.makeText(this, "¡Pedido confirmado! ✓", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun setupBotones() {
        binding.btnBack.setOnClickListener { finish() }

        binding.btnConfirmar.setOnClickListener {
            val total = productos.sumOf { it.precioUnitario * it.cantidad } + 15.0
            val intent = Intent(this, PagoWebViewActivity::class.java)
            intent.putExtra("total", total)
            pagoLauncher.launch(intent)
        }
    }
}