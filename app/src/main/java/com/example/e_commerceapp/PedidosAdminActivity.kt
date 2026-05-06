package com.example.e_commerceapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceapp.databinding.ActivityPedidosAdminBinding

// ── Adapter Peticiones ───────────────────────────────────────
class PeticionesAdapter(
    private val lista: List<Peticion>,
    private val onClick: (Peticion) -> Unit
) : RecyclerView.Adapter<PeticionesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvIcono:    TextView = view.findViewById(R.id.tvIcono)
        val tvTipo:     TextView = view.findViewById(R.id.tvTipo)
        val tvVendedor: TextView = view.findViewById(R.id.tvVendedor)
        val tvFecha:    TextView = view.findViewById(R.id.tvFecha)
        val tvEstado:   TextView = view.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_peticion, parent, false))

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = lista[position]
        holder.tvTipo.text     = p.tipo
        holder.tvVendedor.text = "Vendedor: ${p.vendedor}"
        holder.tvFecha.text    = p.fecha

        val (icono, bgEstado, bgIcono) = when (p.estado) {
            "Pendiente"   -> Triple("📋", "#E67E22", "#1E90FF")
            "En revisión" -> Triple("✏️", "#3498DB", "#4CAF50")
            "Aprobada"    -> Triple("✅", "#4CAF50", "#E67E22")
            "Rechazada"   -> Triple("🗑", "#E74C3C", "#E74C3C")
            else          -> Triple("📋", "#8899AA", "#8899AA")
        }
        holder.tvIcono.text = when (p.tipoPeticion) {
            TipoPeticion.CREACION_TIENDA       -> "🏪"
            TipoPeticion.MODIFICACION_TIENDA   -> "✏️"
            TipoPeticion.NUEVO_PRODUCTO        -> "📦"
            TipoPeticion.MODIFICACION_PRODUCTO -> "🔧"
            TipoPeticion.ELIMINACION_PRODUCTO  -> "🗑"
        }
        holder.tvEstado.text = p.estado
        holder.tvEstado.setBackgroundColor(Color.parseColor(bgEstado))
        holder.itemView.setOnClickListener { onClick(p) }
    }
}

// ── Adapter Pedidos Admin ────────────────────────────────────
class PedidosAdminAdapter(
    private var lista: List<PedidoAdmin>,
    private val onClick: (PedidoAdmin) -> Unit
) : RecyclerView.Adapter<PedidosAdminAdapter.ViewHolder>() {

    private var listaOriginal = lista

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNumero:    TextView = view.findViewById(R.id.tvNumeroPedido)
        val tvCliente:   TextView = view.findViewById(R.id.tvCliente)
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
        holder.tvCliente.text   = "Cliente: ${p.cliente}"
        holder.tvFecha.text     = p.fecha
        holder.tvProductos.text = "${p.cantidadProductos} productos"
        holder.tvTotal.text     = p.total
        holder.tvEstado.text    = p.estado
        val bgColor = when (p.estado) {
            "Pendiente"  -> "#E67E22"
            "En proceso" -> "#1E90FF"
            "Completado" -> "#4CAF50"
            "Cancelado"  -> "#E74C3C"
            else         -> "#8899AA"
        }
        holder.tvEstado.setBackgroundColor(Color.parseColor(bgColor))
        holder.tvEstado.setTextColor(Color.WHITE)
        holder.itemView.setOnClickListener { onClick(p) }
    }

    fun filtrar(texto: String) {
        lista = if (texto.isEmpty()) listaOriginal
        else listaOriginal.filter {
            it.numero.contains(texto, ignoreCase = true) ||
                    it.cliente.contains(texto, ignoreCase = true)
        }
        notifyDataSetChanged()
    }

    fun filtrarPorEstado(estado: String) {
        lista = if (estado == "Todos") listaOriginal
        else listaOriginal.filter { it.estado == estado }
        notifyDataSetChanged()
    }
}

// ── Activity principal ───────────────────────────────────────
class PedidosAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPedidosAdminBinding
    private lateinit var pedidosAdapter: PedidosAdminAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidosAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupTabs()
        setupPeticiones()
        setupPedidosClientes()
        setupBuscador()
        setupSubTabs()
    }

    private fun setupTabs() {
        binding.tabPeticiones.setOnClickListener {
            binding.viewPeticiones.visibility      = View.VISIBLE
            binding.viewPedidosClientes.visibility = View.GONE
            binding.tabPeticiones.setTextColor(Color.parseColor("#1E90FF"))
            binding.tabPedidosClientes.setTextColor(Color.parseColor("#8899AA"))
        }
        binding.tabPedidosClientes.setOnClickListener {
            binding.viewPeticiones.visibility      = View.GONE
            binding.viewPedidosClientes.visibility = View.VISIBLE
            binding.tabPedidosClientes.setTextColor(Color.parseColor("#1E90FF"))
            binding.tabPeticiones.setTextColor(Color.parseColor("#8899AA"))
        }
    }

    private fun setupPeticiones() {
        val adapter = PeticionesAdapter(DatosPedidosAdmin.peticiones) { peticion ->
            val intent = Intent(this, DetallePeticionActivity::class.java)
            intent.putExtra("peticion_id", peticion.id)
            startActivity(intent)
        }
        binding.rvPeticiones.layoutManager = LinearLayoutManager(this)
        binding.rvPeticiones.adapter = adapter
    }

    private fun setupPedidosClientes() {
        pedidosAdapter = PedidosAdminAdapter(DatosPedidosAdmin.pedidos) { pedido ->
            val intent = Intent(this, DetallePedidoAdminActivity::class.java).apply {
                putExtra("numero",     pedido.numero)
                putExtra("cliente",    pedido.cliente)
                putExtra("email",      pedido.emailCliente)
                putExtra("telefono",   pedido.telefonoCliente)
                putExtra("fecha",      pedido.fecha)
                putExtra("total",      pedido.total)
                putExtra("subtotal",   pedido.subtotal)
                putExtra("envio",      pedido.envio)
                putExtra("estado",     pedido.estado)
                putExtra("direccion",  pedido.direccion)
                putExtra("referencia", pedido.referencia)
                putExtra("metodoPago", pedido.metodoPago)
            }
            startActivity(intent)
        }
        binding.rvPedidosClientes.layoutManager = LinearLayoutManager(this)
        binding.rvPedidosClientes.adapter = pedidosAdapter
    }

    private fun setupBuscador() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { pedidosAdapter.filtrar(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupSubTabs() {
        fun setActivo(tab: TextView) {
            listOf(binding.subTabTodos, binding.subTabPendientes,
                binding.subTabEnProceso, binding.subTabCancelados).forEach {
                it.setBackgroundColor(Color.parseColor("#1E2D3D"))
                it.setTextColor(Color.parseColor("#8899AA"))
            }
            tab.setBackgroundColor(Color.parseColor("#1E90FF"))
            tab.setTextColor(Color.WHITE)
        }
        binding.subTabTodos.setOnClickListener      { pedidosAdapter.filtrarPorEstado("Todos");      setActivo(binding.subTabTodos) }
        binding.subTabPendientes.setOnClickListener { pedidosAdapter.filtrarPorEstado("Pendiente");  setActivo(binding.subTabPendientes) }
        binding.subTabEnProceso.setOnClickListener  { pedidosAdapter.filtrarPorEstado("En proceso"); setActivo(binding.subTabEnProceso) }
        binding.subTabCancelados.setOnClickListener { pedidosAdapter.filtrarPorEstado("Cancelado");  setActivo(binding.subTabCancelados) }
    }
}