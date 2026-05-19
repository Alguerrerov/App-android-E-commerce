package com.example.e_commerceapp.client

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerceapp.R
import com.example.e_commerceapp.databinding.ActivityMisPedidosBinding

data class PedidoCliente(
    val numero: String,
    val fecha: String,
    val cantidadProductos: Int,
    val total: String,
    val estado: String
)

class MisPedidosAdapter(
    private var lista: List<PedidoCliente>
) : RecyclerView.Adapter<MisPedidosAdapter.ViewHolder>() {

    private var listaOriginal = lista

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNumero:    TextView = view.findViewById(R.id.tvNumeroPedido)
        val tvFecha:     TextView = view.findViewById(R.id.tvFecha)
        val tvProductos: TextView = view.findViewById(R.id.tvProductos)
        val tvTotal:     TextView = view.findViewById(R.id.tvTotal)
        val tvEstado:    TextView = view.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido_cliente, parent, false))

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
            "Enviado"    -> "#1E90FF"
            "Pendiente"  -> "#E67E22"
            "Cancelado"  -> "#E74C3C"
            else         -> "#8899AA"
        }
        holder.tvEstado.setBackgroundColor(Color.parseColor(bgColor))
    }

    fun filtrarPorEstado(estado: String) {
        lista = if (estado == "Todos") listaOriginal
        else listaOriginal.filter { it.estado == estado }
        notifyDataSetChanged()
    }
}

class MisPedidosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMisPedidosBinding
    private lateinit var adapter: MisPedidosAdapter

    private val pedidos = listOf(
        PedidoCliente("Pedido #A1-0001", "15 de marzo 202X", 3, "S/ XXX,XXX", "Entregado"),
        PedidoCliente("Pedido #E1-0002", "1 de febrero 202X", 3, "S/ XXX,XXX", "Entregado"),
        PedidoCliente("Pedido #B1-0003", "20 de enero 202X",  2, "S/ XXX,XXX", "Enviado")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMisPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MisPedidosAdapter(pedidos)
        binding.rvPedidos.layoutManager = LinearLayoutManager(this)
        binding.rvPedidos.adapter = adapter

        binding.btnBack.setOnClickListener { finish() }

        setupTabs()
    }

    private fun setupTabs() {
        fun setActivo(tab: TextView) {
            listOf(binding.tabTodos, binding.tabPendientes, binding.tabEnviados,
                binding.tabEntregados, binding.tabCancelados).forEach {
                it.setBackgroundColor(Color.parseColor("#1E2D3D"))
                it.setTextColor(Color.parseColor("#8899AA"))
            }
            tab.setBackgroundColor(Color.parseColor("#1E90FF"))
            tab.setTextColor(Color.WHITE)
        }
        binding.tabTodos.setOnClickListener      { adapter.filtrarPorEstado("Todos");     setActivo(binding.tabTodos) }
        binding.tabPendientes.setOnClickListener { adapter.filtrarPorEstado("Pendiente"); setActivo(binding.tabPendientes) }
        binding.tabEnviados.setOnClickListener   { adapter.filtrarPorEstado("Enviado");   setActivo(binding.tabEnviados) }
        binding.tabEntregados.setOnClickListener { adapter.filtrarPorEstado("Entregado"); setActivo(binding.tabEntregados) }
        binding.tabCancelados.setOnClickListener { adapter.filtrarPorEstado("Cancelado"); setActivo(binding.tabCancelados) }
    }
}