package com.example.e_commerceapp

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerceapp.databinding.ActivityDetallePeticionBinding

class DetallePeticionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetallePeticionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallePeticionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("peticion_id") ?: return
        val peticion = DatosPedidosAdmin.peticiones.find { it.id == id } ?: return

        binding.tvTipo.text     = peticion.tipo
        binding.tvVendedor.text = "Vendedor: ${peticion.vendedor}"
        binding.tvFecha.text    = peticion.fecha
        binding.tvEstado.text   = peticion.estado

        val bgEstado = when (peticion.estado) {
            "Pendiente"   -> "#E67E22"
            "En revisión" -> "#3498DB"
            "Aprobada"    -> "#4CAF50"
            "Rechazada"   -> "#E74C3C"
            else          -> "#8899AA"
        }
        binding.tvEstado.setBackgroundColor(Color.parseColor(bgEstado))

        // Info vendedor
        binding.tvNombreVendedor.text  = peticion.vendedor
        binding.tvEmailVendedor.text   = peticion.emailVendedor
        binding.tvTelefonoVendedor.text= "📞 ${peticion.telefonoVendedor}"

        // Contenido dinámico según tipo
        when (peticion.tipoPeticion) {
            TipoPeticion.CREACION_TIENDA -> mostrarCreacionTienda(peticion)
            TipoPeticion.MODIFICACION_TIENDA,
            TipoPeticion.MODIFICACION_PRODUCTO -> mostrarCambios(peticion)
            TipoPeticion.NUEVO_PRODUCTO -> mostrarNuevoProducto(peticion)
            TipoPeticion.ELIMINACION_PRODUCTO -> { /* solo info básica */ }
        }

        if (peticion.notas.isNotEmpty()) {
            binding.tvNotas.text       = peticion.notas
            binding.layoutNotas.visibility = android.view.View.VISIBLE
        }

        // Spinner estado
        val estados = listOf("Pendiente", "En revisión", "Aprobada", "Rechazada")
        binding.spinnerEstado.adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, estados).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerEstado.setSelection(estados.indexOf(peticion.estado).coerceAtLeast(0))

        binding.btnActualizar.setOnClickListener {
            Toast.makeText(this, "Estado actualizado", Toast.LENGTH_SHORT).show()
            finish()
        }
        binding.btnCancelar.setOnClickListener { finish() }
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun mostrarCreacionTienda(p: Peticion) {
        binding.layoutInfoTienda.visibility = android.view.View.VISIBLE
        binding.tvNombreTienda.text  = p.nombreTienda
        binding.tvCategoriaTienda.text = p.categoria
        binding.tvDireccionTienda.text = p.direccion
        binding.tvDescripcionTienda.text = p.descripcion
    }

    private fun mostrarCambios(p: Peticion) {
        binding.layoutCambios.visibility = android.view.View.VISIBLE
        binding.tvMotivoCambio.text = p.motivo
        p.cambiosActual.keys.forEachIndexed { i, campo ->
            val fila = LayoutInflater.from(this).inflate(R.layout.item_fila_cambio, binding.tablaCambios, false)
            fila.findViewById<TextView>(R.id.tvCampo).text    = campo
            fila.findViewById<TextView>(R.id.tvActual).text   = p.cambiosActual[campo]
            fila.findViewById<TextView>(R.id.tvSolicitado).text = p.cambiosSolicitado[campo]
            binding.tablaCambios.addView(fila)
        }
    }

    private fun mostrarNuevoProducto(p: Peticion) {
        binding.layoutProducto.visibility = android.view.View.VISIBLE
        binding.tvNombreProducto.text  = p.nombreProducto
        binding.tvCategoriaProducto.text = p.categoria
        binding.tvMarca.text           = p.marca
        binding.tvSku.text             = p.sku
        binding.tvPrecioProducto.text  = p.precio
        binding.tvStockProducto.text   = p.stock
        binding.tvDescripcionProducto.text = p.descripcionProducto
    }
}