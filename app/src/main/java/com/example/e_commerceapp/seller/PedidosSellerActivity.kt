package com.example.e_commerceapp.seller

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
import com.example.e_commerceapp.R
import com.example.e_commerceapp.databinding.ActivityPedidosSellerBinding

data class ProductoPedido(
    val nombre: String,
    val precioUnitario: String,
    val cantidad: Int,
    val subtotal: String
)

data class Pedido(
    val numero: String,
    val cliente: String,
    val emailCliente: String,
    val telefonoCliente: String,
    val fecha: String,
    val cantidadProductos: Int,
    val total: String,
    val subtotal: String,
    val envio: String,
    val estado: String,
    val direccion: String,
    val referencia: String,
    val productos: List<ProductoPedido>
)

class PedidosAdapter(
    private var lista: List<Pedido>,
    private val onClick: (Pedido) -> Unit
) : RecyclerView.Adapter<PedidosAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNumero:    TextView = view.findViewById(R.id.tvNumeroPedido)
        val tvCliente:   TextView = view.findViewById(R.id.tvCliente)
        val tvFecha:     TextView = view.findViewById(R.id.tvFecha)
        val tvProductos: TextView = view.findViewById(R.id.tvProductos)
        val tvTotal:     TextView = view.findViewById(R.id.tvTotal)
        val tvEstado:    TextView = view.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = lista[position]
        holder.tvNumero.text    = p.numero
        holder.tvCliente.text   = "Cliente: ${p.cliente}"
        holder.tvFecha.text     = p.fecha
        holder.tvProductos.text = "${p.cantidadProductos} productos"
        holder.tvTotal.text     = p.total
        holder.tvEstado.text    = p.estado

        val (bgColor, textColor) = when (p.estado) {
            "Pendiente"   -> "#E67E22" to "#FFFFFF"
            "En proceso"  -> "#1E90FF" to "#FFFFFF"
            "Completado"  -> "#4CAF50" to "#FFFFFF"
            "Cancelado"   -> "#E74C3C" to "#FFFFFF"
            else          -> "#8899AA" to "#FFFFFF"
        }
        holder.tvEstado.setBackgroundColor(Color.parseColor(bgColor))
        holder.tvEstado.setTextColor(Color.parseColor(textColor))
        holder.itemView.setOnClickListener { onClick(p) }
    }

    override fun getItemCount() = lista.size

    private var listaOriginal: List<Pedido> = lista

    fun setLista(nueva: List<Pedido>) {
        listaOriginal = nueva
        lista = nueva
        notifyDataSetChanged()
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

class PedidosSellerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPedidosSellerBinding
    private lateinit var adapter: PedidosAdapter

    private val pedidos = listOf(
        Pedido("#PED-1058", "Juan Pérez", "juan.perez@gmail.com", "987 654 321",
            "16 May 2024 · 10:30 a.m.", 3, "S/ 250.00", "S/ 350.00", "S/ 10.00",
            "Pendiente", "Av. Los Próceres 123, Lima\nLima, Perú",
            "Frente al parque principal",
            listOf(
                ProductoPedido("Filtro de aceite Mann W712", "S/ 45.00", 1, "S/ 45.00"),
                ProductoPedido("Pastillas de freno Brembo P85020", "S/ 120.00", 2, "S/ 240.00"),
                ProductoPedido("Aceite Mobil 1 5W-30 (1L)", "S/ 65.00", 1, "S/ 65.00")
            )),
        Pedido("#PED-1057", "María Gómez", "maria.gomez@gmail.com", "976 543 210",
            "16 May 2024 · 09:15 a.m.", 2, "S/ 180.00", "S/ 170.00", "S/ 10.00",
            "En proceso", "Jr. Moquegua 456, Lima", "Edificio azul",
            listOf(
                ProductoPedido("Filtro de Aire Mann", "S/ 85.00", 2, "S/ 170.00")
            )),
        Pedido("#PED-1056", "Carlos López", "carlos.lopez@gmail.com", "965 432 109",
            "15 May 2024 · 04:45 p.m.", 4, "S/ 320.00", "S/ 310.00", "S/ 10.00",
            "En proceso", "Av. Arequipa 789, Lima", "Cerca al semáforo",
            listOf(
                ProductoPedido("Bujía NGK Iridium", "S/ 77.50", 4, "S/ 310.00")
            )),
        Pedido("#PED-1055", "Ana Torres", "ana.torres@gmail.com", "954 321 098",
            "15 May 2024 · 11:20 a.m.", 1, "S/ 95.00", "S/ 85.00", "S/ 10.00",
            "Completado", "Calle Las Flores 321, Lima", "Casa con reja verde",
            listOf(
                ProductoPedido("Aceite Mobil 1 5W-30 Sintético 4L", "S/ 85.00", 1, "S/ 85.00")
            )),
        Pedido("#PED-1053", "Pedro Ramírez", "pedro.ramirez@gmail.com", "943 210 987",
            "14 May 2024 · 02:10 p.m.", 1, "S/ 75.00", "S/ 65.00", "S/ 10.00",
            "Cancelado", "Av. Brasil 654, Lima", "Frente al banco",
            listOf(
                ProductoPedido("Filtro de Aceite Mann W610/3", "S/ 65.00", 1, "S/ 65.00")
            ))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidosSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupBuscador()
        setupTabs()
    }

    private fun setupRecyclerView() {
        adapter = PedidosAdapter(pedidos) { pedido ->
            val intent = Intent(this, DetallePedidoSellerActivity::class.java).apply {
                putExtra("numero",    pedido.numero)
                putExtra("cliente",   pedido.cliente)
                putExtra("email",     pedido.emailCliente)
                putExtra("telefono",  pedido.telefonoCliente)
                putExtra("fecha",     pedido.fecha)
                putExtra("total",     pedido.total)
                putExtra("subtotal",  pedido.subtotal)
                putExtra("envio",     pedido.envio)
                putExtra("estado",    pedido.estado)
                putExtra("direccion", pedido.direccion)
                putExtra("referencia",pedido.referencia)
            }
            startActivity(intent)
        }
        adapter.setLista(pedidos)
        binding.rvPedidos.layoutManager = LinearLayoutManager(this)
        binding.rvPedidos.adapter = adapter
    }

    private fun setupBuscador() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { adapter.filtrar(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupTabs() {
        setTabActivo(binding.tabTodos)
        binding.tabTodos.setOnClickListener {
            adapter.filtrarPorEstado("Todos")
            setTabActivo(binding.tabTodos)
        }
        binding.tabPendientes.setOnClickListener {
            adapter.filtrarPorEstado("Pendiente")
            setTabActivo(binding.tabPendientes)
        }
        binding.tabEnProceso.setOnClickListener {
            adapter.filtrarPorEstado("En proceso")
            setTabActivo(binding.tabEnProceso)
        }
        binding.tabCompletados.setOnClickListener {
            adapter.filtrarPorEstado("Completado")
            setTabActivo(binding.tabCompletados)
        }
    }

    private fun setTabActivo(tab: TextView) {
        listOf(binding.tabTodos, binding.tabPendientes, binding.tabEnProceso, binding.tabCompletados)
            .forEach { it.setTextColor(Color.parseColor("#8899AA")) }
        tab.setTextColor(Color.parseColor("#1E90FF"))
    }
}