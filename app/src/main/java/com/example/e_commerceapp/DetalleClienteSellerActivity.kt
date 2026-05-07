package com.example.e_commerceapp

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.example.e_commerceapp.databinding.ActivityDetalleClienteSellerBinding

class HistorialAdapter(
    private val lista: List<PedidoResumen>
) : RecyclerView.Adapter<HistorialAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNumero:    TextView = view.findViewById(R.id.tvNumeroPedido)
        val tvFecha:     TextView = view.findViewById(R.id.tvFecha)
        val tvProductos: TextView = view.findViewById(R.id.tvProductos)
        val tvTotal:     TextView = view.findViewById(R.id.tvTotal)
        val tvEstado:    TextView = view.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pedido, parent, false))

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = lista[position]
        holder.tvNumero.text    = p.numero
        holder.tvFecha.text     = p.fecha
        holder.tvProductos.text = "${p.cantidadProductos} productos"
        holder.tvTotal.text     = p.total
        holder.tvEstado.text    = p.estado
        val bgColor = when (p.estado) {
            "Entregado"  -> "#4CAF50"
            "Cancelado"  -> "#E74C3C"
            "En proceso" -> "#1E90FF"
            else         -> "#8899AA"
        }
        holder.tvEstado.setBackgroundColor(Color.parseColor(bgColor))
        holder.tvEstado.setTextColor(Color.WHITE)
    }
}

class DetalleClienteSellerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleClienteSellerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleClienteSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nombre          = intent.getStringExtra("nombre")          ?: ""
        val apellidos       = intent.getStringExtra("apellidos")       ?: ""
        val email           = intent.getStringExtra("email")           ?: ""
        val telefono        = intent.getStringExtra("telefono")        ?: ""
        val dni             = intent.getStringExtra("dni")             ?: ""
        val fechaNacimiento = intent.getStringExtra("fechaNacimiento") ?: ""
        val direccion       = intent.getStringExtra("direccion")       ?: ""
        val metodoPago      = intent.getStringExtra("metodoPago")      ?: ""
        val estado          = intent.getStringExtra("estado")          ?: ""
        val clienteDesde    = intent.getStringExtra("clienteDesde")    ?: ""

        // Header
        binding.tvNombre.text       = nombre
        binding.tvEstado.text       = estado
        binding.tvEmail.text        = "✉ $email"
        binding.tvTelefono.text     = "📞 $telefono"
        binding.tvClienteDesde.text = "🗓 $clienteDesde"

        val bgEstado = if (estado == "Activo") "#4CAF50" else "#8899AA"
        binding.tvEstado.setBackgroundColor(Color.parseColor(bgEstado))

        // Info personal
        binding.tvNombres.text         = nombre
        binding.tvApellidos.text       = apellidos
        binding.tvCorreo.text          = email
        binding.tvTelefonoInfo.text    = telefono
        binding.tvDni.text             = dni
        binding.tvFechaNacimiento.text = fechaNacimiento
        binding.tvDireccion.text       = direccion
        binding.tvMetodoPago.text      = metodoPago
        binding.tvEstadoInfo.text      = estado
        binding.tvEstadoInfo.setTextColor(
            if (estado == "Activo") Color.parseColor("#4CAF50")
            else Color.parseColor("#8899AA")
        )

        // Tabs
        binding.tabInformacion.setOnClickListener {
            binding.viewInformacion.visibility = View.VISIBLE
            binding.viewHistorial.visibility   = View.GONE
            binding.tabInformacion.setTextColor(Color.parseColor("#1E90FF"))
            binding.tabHistorial.setTextColor(Color.parseColor("#8899AA"))
        }
        binding.tabHistorial.setOnClickListener {
            binding.viewInformacion.visibility = View.GONE
            binding.viewHistorial.visibility   = View.VISIBLE
            binding.tabHistorial.setTextColor(Color.parseColor("#1E90FF"))
            binding.tabInformacion.setTextColor(Color.parseColor("#8899AA"))
        }

        // Historial — datos hardcodeados por cliente
        val historial = listOf(
            PedidoResumen("#PED-1058", "16 May 2024 · 10:30 a.m.", 3, "S/ 360.00", "Entregado"),
            PedidoResumen("#PED-1023", "30 Abr 2024 · 09:15 a.m.", 2, "S/ 180.00", "Entregado"),
            PedidoResumen("#PED-0987", "18 Abr 2024 · 02:45 p.m.", 4, "S/ 420.00", "Entregado"),
            PedidoResumen("#PED-0912", "05 Abr 2024 · 11:20 a.m.", 1, "S/ 75.00",  "Cancelado"),
            PedidoResumen("#PED-0876", "28 Mar 2024 · 04:10 p.m.", 2, "S/ 220.00", "Entregado")
        )
        binding.viewHistorial.layoutManager = LinearLayoutManager(this)
        binding.viewHistorial.adapter = HistorialAdapter(historial)

        binding.btnBack.setOnClickListener { finish() }
    }
}